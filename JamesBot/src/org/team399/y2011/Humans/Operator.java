/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.Humans;
import org.team399.y2011.HumanInterfaceDevices.Rumblepad2GamePad;
import org.team399.y2011.robot.JamesBot;
import org.team399.y2011.robot.actuators.Arm;

/**
 * Operator object. Used to clean up main teleop loop
 * @author Jeremy Germita
 */
public class Operator {

    /**
     * Constructor.
     * @param who Who is operating?
     */
    public Operator(String who) {
        System.out.println(who + " is operating!");
    }

    /**
     * Initialize the operator's mechanisms
     * @param armSetPoint The setpoint to set the arm at the beginning of teleop
     * @param speedLimit The arm speed limit
     */
    public void init(double armSetPoint, double speedLimit) {
        JamesBot.arm.setPoint(armSetPoint);     //set the arm setpoint
        JamesBot.arm.reset();                   //reset the arm PID
        JamesBot.arm.update();
        JamesBot.arm.enable();
        JamesBot.arm.setSpeedLimit(speedLimit);
    }

    int currentPoint = 0;
    boolean prevPad = false, currPad = false;

    double currDelta = 0, prevDelta = 0;
    double setPnt = 0, prevPnt = 0;
    /**
     * Do operator stuff
     */
    public void operate() {
        /**********************************************************************
         * Arm stuff
         */
        //if(JamesBot.io.getLeftToggleSwitch()) {
        //JamesBot.arm.safeMode(JamesBot.gamePad.getRightY(), false);

        
            JamesBot.arm.enable();
            currPad = JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.UP) ||
                      JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.DOWN);  //currPad is true if a button is pressed

            currDelta = JamesBot.arm.getSetpoint() + (JamesBot.gamePad.getRightY()*.02);

//            if(JamesBot.gamePad.getLeftY() < -.8) {
//                currentPoint = 4;
//            } else if(JamesBot.gamePad.getLeftY() > .8) {
//                currentPoint = 1;
//            } else {
//                if(JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.UP) && currPad != prevPad) {
//                    currentPoint++;
//                    currentPoint = (currentPoint <= 0) ? 0 :                //Limit currentPoint from 0-5
//                                  ((currentPoint >= 5) ? 5 : currentPoint); //Nested ternary operators FTW
//                    switch(currentPoint) {
//                        case 0: JamesBot.arm.setPoint(Arm.ArmStates.INSIDE);        break;   //Stowed setpoint
//                        case 1: JamesBot.arm.setPoint(Arm.ArmStates.GROUND);        break;   //Ground pickup setpoint
//                        case 2: JamesBot.arm.setPoint(Arm.ArmStates.LOW);           break;   //Low Peg Setpoint
//                        case 3: JamesBot.arm.setPoint(Arm.ArmStates.MID);           break;   //Middle peg Setpoint
//                        case 4: JamesBot.arm.setPoint(Arm.ArmStates.HIGH);          break;   //High peg setpoint
//                        case 5: JamesBot.arm.setPoint(Arm.ArmStates.TOMAHAWK_HIGH); break;   //Tomahawk high peg setpoint
//                    }
//                } else if(JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.DOWN) && currPad != prevPad)  {
//                    currentPoint--;
//                    currentPoint = (currentPoint <= 0) ? 0 :                //Limit currentPoint from 0-5
//                                  ((currentPoint >= 5) ? 5 : currentPoint); //Nested ternary operators FTW
//                    switch(currentPoint) {
//                        case 0: JamesBot.arm.setPoint(Arm.ArmStates.INSIDE);        break;   //Stowed setpoint
//                        case 1: JamesBot.arm.setPoint(Arm.ArmStates.GROUND);        break;   //Ground pickup setpoint
//                        case 2: JamesBot.arm.setPoint(Arm.ArmStates.LOW);           break;   //Low Peg Setpoint
//                        case 3: JamesBot.arm.setPoint(Arm.ArmStates.MID);           break;   //Middle peg Setpoint
//                        case 4: JamesBot.arm.setPoint(Arm.ArmStates.HIGH);          break;   //High peg setpoint
//                        case 5: JamesBot.arm.setPoint(Arm.ArmStates.TOMAHAWK_HIGH); break;   //Tomahawk high peg setpoint
//                    }
                //} else if(JamesBot.gamePad.getButton(11)) {
                //    JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
                //} else if(JamesBot.) {
                if(JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.DOWN)) {
                    JamesBot.arm.setPoint(Arm.ArmStates.GROUND);
                } else if(JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.UP)) {
                    JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
                }else {
                    if(Math.abs(JamesBot.gamePad.getRightY()) > 0.1) {
                        JamesBot.arm.setPoint(currDelta);   //Arm Delta control
                    }


                    if(JamesBot.arm.getPosition() == Arm.ArmStates.GROUND) {    currentPoint = 1;}   //Sets currentPoint if the arm is moved manually
                    else if(JamesBot.arm.getPosition() == Arm.ArmStates.LOW) { currentPoint = 2;}
                    else if(JamesBot.arm.getPosition() == Arm.ArmStates.MID) { currentPoint = 3;}
                    else if(JamesBot.arm.getPosition() == Arm.ArmStates.HIGH) { currentPoint = 4;}
                }
            //}


            JamesBot.arm.update();

            JamesBot.arm.fold(JamesBot.gamePad.getButton(10) || JamesBot.gamePad.getButton(12));


            prevPad = currPad;
        //} else {
        //    JamesBot.arm.safeMode(JamesBot.gamePad.getRightY(), false);
        //}

        /**********************************************************************
         * End Arm Code
         */

        /**********************************************************************
         * Other gamePad controls
         */

        //Endgame Stuff:

        JamesBot.flopper.flop(JamesBot.gamePad.getButton(9) || JamesBot.io.getMissileSwitch());   //Flop with either a gamepad button or the missile switch
        JamesBot.deploy.deploy(JamesBot.gamePad.getButton(1) && JamesBot.gamePad.getButton(2) || //Deploy with either a gamepad button or
                ((JamesBot.io.getBlackButton() || JamesBot.io.getBlueButton() ||          //The missile switch with any button on the button panel
                  JamesBot.io.getRedButton() || JamesBot.io.getWhiteButton())) &&
                  JamesBot.io.getMissileSwitch());

        //Roller Claw Stuff:
        if(JamesBot.gamePad.getButton(6)) {
            JamesBot.roller.grab(1);                    // Grab tube
        } else if(JamesBot.gamePad.getButton(8)) {
            JamesBot.roller.grab(-.5);                   // release tube
        } else if(JamesBot.gamePad.getButton(7)) {
            JamesBot.roller.articulate(.5);              // articulate tube up
        } else if(JamesBot.gamePad.getButton(5)) {
            JamesBot.roller.articulate(-.5);             // articulate tube down
        } else {
            JamesBot.roller.grab(0);

        }
    }

}
