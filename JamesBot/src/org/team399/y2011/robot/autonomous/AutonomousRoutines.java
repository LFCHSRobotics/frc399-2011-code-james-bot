/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2011.robot.autonomous;

import org.team399.y2011.robot.JamesBot;
import org.team399.y2011.robot.actuators.Arm;

/**
 *
 * @author Jeremy Germita and Robert Wong
 */
public class AutonomousRoutines {

    private static long startTime;  //Start time variable for timer functions

    /**
     * Initialize dumbAuton components
     */
    public static void initDumbAuton() {
        startTime = System.currentTimeMillis(); //Initialize timer
        JamesBot.arm.setElbow(false);           //Hold arm in
        JamesBot.arm.enable();                  //Enable closed loop control on arm
        JamesBot.arm.setSpeedLimit(.7);         //Speed limit on arm = .7
    }

    /**
     * Actual dumbAuton code
     * This autonomous program brought us to the finals in the San Diego Regional
     */
    public static void dumbAuton() {
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);  //Move arm up

        JamesBot.arm.update();  //Update arm pid to keep position

        if (System.currentTimeMillis() - startTime > 3100 && System.currentTimeMillis() - startTime < 9750) {
            JamesBot.robot.arcadeDrive(.5, 0);  //Drive forward at half power for 6.65 seconds
        }
        if (System.currentTimeMillis() - startTime > 9750 && !(System.currentTimeMillis() - startTime > 11000)) {
            JamesBot.robot.arcadeDrive(0, 0);   //Stop
            JamesBot.roller.grab(-.5);          //Release tube for 1.25 seconds
        }

        if (System.currentTimeMillis() - startTime > 11000) {
            JamesBot.robot.arcadeDrive(-.5, 0); //Drive backwards for 4 seconds
        }

    }

    /**
     * Initialize all compeonents for competent auton
     */
    public static void initCompetentAuton() {
        startTime = System.currentTimeMillis();              //Start the timer
        JamesBot.arm.setElbow(false);                        //unfold arm
        JamesBot.arm.enable();                               //Enable closed loop control on arm
        JamesBot.arm.setSpeedLimit(1);                       //Set speed limit on arm to maximum
        JamesBot.arm.setPoint((Arm.ArmStates.HIGH) + .075);  //Bring the arm to scoring position, add .075 to adjust.**
        JamesBot.robot.resetGyro();                          //Reset the gyroscope

        JamesBot.robot.lowGear();                            //Low Gear
        //** For some odd reason, the arm's setpoints are much higher in autonomous than they are in teleop
        //   Adjusted setpoint to account for that
    }
    //Variables indicating the different states of autonomous, for CompetentAuton
    static boolean driveForward;    //State: Driving forward(Also, bring arm up, unfold)
    static boolean releaseTube;     //State: Releasing tube
    static boolean pullBack;        //State: Pulling back(Also, Bring arm down, fold)
    static boolean turnAround;      //State: Turn robot around

    /*public interface COMPETENT_AUTON_TIMES {
    public static long START           = 0;
    public static long END_FWD_DRIVE   = START + 6700;
    public static long BEGIN_RELEASE   = END_FWD_DRIVE;
    public static long END_RELEASE     = BEGIN_RELEASE + 5000;
    public static long BEGIN_REV_DRIVE = END_RELEASE;
    public static long END_REV_DRIVE   = BEGIN_REV_DRIVE + 1300;
    psl
    }*/
    /**
     * Competent Auton code
     * This autonomous program Helped us to win the Utah regional
     */
    public static void competentAuton() {


        //JamesBot.db.autonomousPackAll();

        driveForward = System.currentTimeMillis() - startTime > 0 && System.currentTimeMillis() - startTime < 6700;
        releaseTube = System.currentTimeMillis() - startTime > 6700 && System.currentTimeMillis() - startTime < 7200;
        pullBack = System.currentTimeMillis() - startTime > 7200 && System.currentTimeMillis() - startTime < 8500;
        turnAround = System.currentTimeMillis() - startTime > 12000;

        JamesBot.arm.update();

        if (driveForward) {
            JamesBot.robot.driveStraight(.5, 0);                                        //Drive forward(with heading correction)
            //JamesBot.db.setDiagnosticPrintout("[AUT] Driving Forward. Moving Arm up");  //Print to dash
        }
        if (releaseTube) {
            //JamesBot.db.setDiagnosticPrintout("[AUT] Releasing tube. Moving arm down.");//Print to dash
            JamesBot.arm.setPoint(((Arm.ArmStates.HIGH) + .17));                        //Move arm Down
            JamesBot.robot.arcadeDrive(0, 0);                                           //Stop robot
            JamesBot.roller.grab(-.5);                                                  //Release claw
            //Idea: if(limitSwitchClaw) { releaseTubeEndTime += 50; }
        }

        if (pullBack) {
            JamesBot.robot.driveStraight(-.7, 0);                           //Drive reverse w/ heading correction
            //JamesBot.db.setDiagnosticPrintout("[AUT] Driving Backward");    //Print to dash
        }

        if (System.currentTimeMillis() - startTime > 9500 && System.currentTimeMillis() - startTime < 10000) {
            //JamesBot.db.setDiagnosticPrintout("[AUT] Folding arm"); //Print to dash
            JamesBot.arm.setPoint(Arm.ArmStates.INSIDE);            //Bring arm down
            JamesBot.roller.grab(0);                                //Stop claw
            JamesBot.arm.setElbow(true);                            //Fold arm
            JamesBot.robot.arcadeDrive(0, 0);                        //Stop robot
        }

        if (turnAround) {
            //JamesBot.db.setDiagnosticPrintout("[AUT] Turning around.");
            if (JamesBot.robot.getAngle() < 170) {
                JamesBot.robot.arcadeDrive(0, -.8);
            } else if (JamesBot.robot.getAngle() > 190) {
                JamesBot.robot.arcadeDrive(0, .8);
            } else {
                JamesBot.robot.arcadeDrive(0, 0);
            }
        }

        if (System.currentTimeMillis() - startTime > 14000) {
            JamesBot.robot.highGear();
        }
    }

    /**
     * Do nothing in autonomous mode. 
     * It works. Proven in the shop.
     * One flaw: does not score
     */
    public static void disabled() {
        System.out.println("Sad robot is sad during autonomous :(");
    }

    public static void initGeniusAuton() {
        startTime = System.currentTimeMillis();              //Start the timer
        //JamesBot.arm.zeroPotentiometer();
        JamesBot.arm.setVoltRampRate(0);
        JamesBot.arm.setElbow(false);                        //unfold arm
        JamesBot.arm.enable();                               //Enable closed loop control on arm
        JamesBot.arm.setSpeedLimit(1);                       //Set speed limit on arm to maximum
        JamesBot.arm.setPoint((Arm.ArmStates.HIGH) + .075);  //Bring the arm to scoring position, add .075 to adjust.**
        JamesBot.robot.resetGyro();                          //Reset the gyroscope
        JamesBot.robot.highGear();

        //** For some odd reason, the arm's setpoints are much higher in autonomous than they are in teleop
        //   Adjusted setpoint to account for that
    }
    private static boolean noTube = false;
    private static boolean beginGrab = false;

    /**
     * Experimental two tube autonomous, modified for one tube for BATB
     */
    public static void geniusAuton() {
        double timeOff = 1000;
        driveForward = System.currentTimeMillis() - startTime > 0 + timeOff && System.currentTimeMillis() - startTime < 2617 + timeOff;
        releaseTube = System.currentTimeMillis() - startTime > 2617 + timeOff && System.currentTimeMillis() - startTime < 3570 + timeOff;
        pullBack = System.currentTimeMillis() - startTime > 3570 + timeOff && System.currentTimeMillis() - startTime < 5477 + timeOff;
        //turnAround   = System.currentTimeMillis() - startTime > 6000;
        boolean turnToTube = System.currentTimeMillis() - startTime > 5477 + timeOff && System.currentTimeMillis() - startTime < 6650 + timeOff;
        boolean grabTube = System.currentTimeMillis() - startTime > 6650 + timeOff && System.currentTimeMillis() - startTime < 7500 + timeOff;
        boolean liftArm = System.currentTimeMillis() - startTime > 7500 + timeOff && System.currentTimeMillis() - startTime < 9750 + timeOff;
        boolean driveToPeg = System.currentTimeMillis() - startTime > 9750 + timeOff && System.currentTimeMillis() - startTime < 11650 + timeOff;
        boolean secondTubeScore = System.currentTimeMillis() - startTime > 11650 + timeOff && System.currentTimeMillis() - startTime < 13000 + timeOff;

        JamesBot.arm.update();

        if (driveForward) {
            //JamesBot.robot.driveStraight(.5, 0);                                        //Drive forward(with heading correction)
            JamesBot.robot.driveToAngle(.6, 0);
            JamesBot.arm.setPoint((Arm.ArmStates.HIGH) + .075);  //Bring the arm to scoring position, add .075 to adjust.**
        }
        if (releaseTube) {
            JamesBot.arm.setPoint(((Arm.ArmStates.HIGH) + .15));                        //Move arm Down
            JamesBot.robot.arcadeDrive(0, 0);                                           //Stop robot
        }
        if (pullBack) {
            JamesBot.roller.grab(-.7);                                                  //Release claw
            JamesBot.robot.driveToAngle(-.7, 0);                           //Drive reverse w/ heading correction
            System.out.println("Driving Back, T: " + (System.currentTimeMillis() - startTime));
        }/*
        if(turnToTube) {
        JamesBot.robot.driveToAngle(0, -50);
        JamesBot.arm.setPoint(Arm.ArmStates.GROUND+.075);
        beginGrab = true;
        }
        if(grabTube) {
        JamesBot.robot.driveToAngle(.5, -50);
        JamesBot.roller.holdTube();
        }
        if(liftArm) {
        JamesBot.robot.arcadeDrive(0,0);
        JamesBot.roller.holdTube();
        JamesBot.arm.setPoint((Arm.ArmStates.HIGH) + .085);

        }
        if(driveToPeg && JamesBot.roller.getLimitSwitch()) {
        JamesBot.arm.setPoint((Arm.ArmStates.HIGH) + .125);
        JamesBot.robot.driveToAngle(.875, 3);
        }
        if(secondTubeScore && JamesBot.roller.getLimitSwitch()) {
        JamesBot.arm.setPoint(((Arm.ArmStates.HIGH) + .15));                        //Move arm Down
        JamesBot.robot.arcadeDrive(0, 0);                                           //Stop robot
        JamesBot.roller.grab(-.5);                                                  //Release claw
        }
        if(System.currentTimeMillis() - startTime > 13000 && System.currentTimeMillis() - startTime < 14500) {
        JamesBot.robot.arcadeDrive(-1, 0);
        }*/
        //if(System.currentTimeMillis() - startTime > 14500 ) {
        if (System.currentTimeMillis() - startTime > 6477) {
            stop();
        }
    }

    public static void stop() {
        JamesBot.robot.arcadeDrive(0, 0);
        JamesBot.roller.grab(0);
        JamesBot.arm.setPoint(Arm.ArmStates.INSIDE);
        JamesBot.arm.setElbow(true);
    }
}
