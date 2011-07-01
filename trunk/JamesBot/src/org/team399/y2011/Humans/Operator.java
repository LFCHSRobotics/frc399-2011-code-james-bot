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
        JamesBot.deploy.beginTimer();
    }

    int currentPoint = 0;
    boolean prevPad = false, currPad = false;

    double currDelta = 0, prevDelta = 0;
    double setPnt = 0, prevPnt = 0;
    /**
     * Do operator stuff
     */
    public void operate() {
        scoringControl();
        endGameControl();        
    }

    public void scoringControl() {
        /**********************************************************************
         * Arm stuff
         */

        JamesBot.arm.enable();

        double deltaGain = .015; //Increasing this will increase the speed at which the delta increases. This will increase arm "Laggy-ness"
        currDelta = JamesBot.arm.getSetpoint() + (JamesBot.gamePad.getRightY()*deltaGain);
        if(JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.DOWN)) {
            JamesBot.arm.setPoint(Arm.ArmStates.GROUND);
        } else if(JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.UP)) {
            JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
        } else if(JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.RIGHT) ||
            JamesBot.gamePad.getDPad(Rumblepad2GamePad.DPadStates.LEFT)) {
            JamesBot.arm.setPoint(Arm.ArmStates.MID);
        } else {
            if(Math.abs(JamesBot.gamePad.getRightY()) > 0.1) {
                JamesBot.arm.setPoint(currDelta);   //Arm Delta control
            }
        }

        JamesBot.arm.update();

        JamesBot.arm.fold(JamesBot.gamePad.getButton(10) || JamesBot.gamePad.getButton(12) || JamesBot.leftJoy.getButton(2));

        //Roller Claw Stuff:
        if(JamesBot.gamePad.getButton(6)) {
            JamesBot.roller.holdTube();
        } else if(JamesBot.gamePad.getButton(8)) {
            JamesBot.roller.spitTube();
        } else if(JamesBot.gamePad.getButton(7)) {
            JamesBot.roller.articulate(.7);              // articulate tube up
        } else if(JamesBot.gamePad.getButton(5)) {
            JamesBot.roller.articulate(-.7);             // articulate tube down
        } else {
            JamesBot.roller.grab(0);
        }

        JamesBot.arm.print();
    }

    public void endGameControl() {

        boolean manualDeploy = JamesBot.gamePad.getButton(1) && JamesBot.gamePad.getButton(2) || //Deploy with either a gamepad button or
                        ((JamesBot.io.getBlackButton() ||JamesBot.io.getWhiteButton()));         //with the arcade butons
        boolean autoTimeDeploy = JamesBot.io.getBlueButton() && JamesBot.io.getRedButton();
        boolean safety = JamesBot.io.getMissileSwitch() || JamesBot.gamePad.getButton(1);

        JamesBot.flopper.flop(JamesBot.gamePad.getButton(9) || JamesBot.io.getMissileSwitch());   //Flop with either a gamepad button or the missile switch

        if(autoTimeDeploy) {                //Deploy with timers
            JamesBot.deploy.timerDeploy();
        } else {                            //Or manually
            if(safety) {
                JamesBot.deploy.deploy(manualDeploy);
            }
        }
    }

}
