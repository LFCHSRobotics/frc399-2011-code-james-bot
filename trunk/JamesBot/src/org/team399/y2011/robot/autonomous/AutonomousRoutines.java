/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.autonomous;
import org.team399.y2011.robot.JamesBot;
import org.team399.y2011.robot.actuators.Arm;
import edu.wpi.first.wpilibj.DriverStation;


/**
 *
 * @author Jeremy Germita and Robert Wong
 */
public class AutonomousRoutines {

    private static String kSide;
    private static DriverStation ds = DriverStation.getInstance();
    
    /**
     * Set the side of the field
     * Depreciated due to abandonment of the line sensors
     * @param side The side
     * @deprecated
     */
    public static void setSide(String side) {
        kSide = side;
    }

    private interface distances {
        double toScoringDistance = 2188; //Currently an arbitrary number, change in testing
        double toCenterLine = -1400;      //Also arbitrary
    }
    private interface speeds {
        double driving = .5;
        double approach = .3;
    }

    private static boolean stepOne = false, stepTwo = false, stepThree = false;

    /**
     * Will not work. Will kill robot.
     * @deprecated
     */
    public static void oneTubeAuton() {
        //JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
        //while(ds.isEnabled() && ds.isAutonomous()) {    //While in autonomous
            //Drive to the scoring distance - 3 feet
            double turnProp = .05;
            JamesBot.robot.resetEncoder();
            while(JamesBot.robot.getEncoderCounts() < distances.toScoringDistance && (ds.isEnabled() && ds.isAutonomous())) { //Change this value
                //JamesBot.arm.update();
                JamesBot.robot.arcadeDrive(speeds.driving, (turnProp)*-JamesBot.robot.getAngle());
            }
            JamesBot.robot.resetEncoder();
            JamesBot.roller.articulate(1);  //Spit tube
        
            while(JamesBot.robot.getEncoderCounts() > distances.toCenterLine && (ds.isEnabled() && ds.isAutonomous())) {
                JamesBot.robot.arcadeDrive(-speeds.driving, (turnProp)*-JamesBot.robot.getAngle());
            }
            JamesBot.robot.arcadeDrive(0,0);
            
        //}
    }

    /**
     * Initialize dumbAuton components
     */
    public static void initDumbAuton() {
        startTime = System.currentTimeMillis();
        JamesBot.arm.setElbow(false);    //Hold arm in
        JamesBot.arm.enable();          //Enable closed loop control on arm
        JamesBot.arm.setSpeedLimit(.7); //Halve the speed limit on the arm
    }

    /**
     * Actual dumbAuton code
     * This autonomous program brought us to the finals in the San Diego Regional
     */
    public static void dumbAuton() {
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
        //JamesBot.arm.fold(true); no move to timer based
        
        JamesBot.arm.update();
        if(System.currentTimeMillis() - startTime > 3100 && System.currentTimeMillis() - startTime < 9750) {
            
            JamesBot.robot.arcadeDrive(.5, 0);
        }
        if(System.currentTimeMillis() - startTime > 9750 && !(System.currentTimeMillis() - startTime > 11000)) {
            JamesBot.robot.arcadeDrive(0, 0);
            JamesBot.roller.grab(-.5);
        }

        if(System.currentTimeMillis() - startTime > 11000) {
            JamesBot.robot.arcadeDrive(-.5, 0);
        }

    }
    
    public static void initCompetentAuton() {
        startTime = System.currentTimeMillis();
        JamesBot.arm.setElbow(false);    //Hold arm in
        JamesBot.arm.enable();          //Enable closed loop control on arm
        JamesBot.arm.setSpeedLimit(.8); //Halve the speed limit on the arm
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
        JamesBot.robot.resetGyro();
    }

    /**
     * Actual dumbAuton code
     * This autonomous program brought us to the finals in the San Diego Regional
     */

    static boolean driveForward;
    static boolean releaseTube;
    static boolean pullBack;
    static boolean turnSlightly;
    static boolean pickUp;
    public static void competentAuton() {

        //JamesBot.arm.fold(true); no move to timer based

        driveForward = System.currentTimeMillis() - startTime > 250  && System.currentTimeMillis() - startTime < 7300;
        releaseTube  = System.currentTimeMillis() - startTime > 7300 && System.currentTimeMillis() - startTime < 7500;
        pullBack     = System.currentTimeMillis() - startTime > 7500 && System.currentTimeMillis() - startTime < 9500;
        JamesBot.arm.update();
        if(driveForward) {
            JamesBot.robot.driveStraight(.5, 0);
        }
        if(releaseTube) {
            JamesBot.robot.arcadeDrive(0, 0);
            JamesBot.roller.grab(-.5);
        }

        if(pullBack) {
            JamesBot.robot.driveStraight(-1, 0);
        }

        if(System.currentTimeMillis() - startTime > 8500) {
            JamesBot.robot.arcadeDrive(0,0);
            JamesBot.arm.setPoint(Arm.ArmStates.GROUND);
            JamesBot.roller.grab(0);
        }

    }

    /**
     * Hold position
     * I am depreciating it because I have never tested this. This will hopefully
     * sit still in auton, but I am not sure if it will let us drive in teleop
     * @deprecated
     */
    public static void hold() {
        double turnProp = .2;
        while(ds.isEnabled() && ds.isAutonomous()) {
            JamesBot.robot.arcadeDrive(0, turnProp*-JamesBot.robot.getAngle());
        }
    }
    /**
     * Track Lines.
     * No longer tracks lines. Only drives forever.
     * Depreciated for a lack of development
     * @deprecated
     */
    public static void deadReckonA() {
        long startTime = System.currentTimeMillis();
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
        while(System.currentTimeMillis() - startTime < 1000) {
            while(System.currentTimeMillis() - startTime < 2000) {
                
                //if(lsa.getA()) {
                    JamesBot.robot.tankDrive(-.5, .5);
                //} else {
                    //Check to see if the drivetrain drifts to the right/left, adjust
                    //JamesBot.robot.tankDrive(-.5, .3);
                //}
            }
            JamesBot.robot.tankDrive(0, 0);
        }
    }

    /**
     * Spin
     * This is a useless autonomous. Just in case we want to just sit out there
     * and look dangerous
     */
    public static void spin() {
        JamesBot.robot.tankDrive(-1, 1);
    }

    /**
     * Do nothing in autonomous mode.
     * It works. Proven in the shop.
     * One flaw: does not score
     */
    public static void disabled() {
        System.out.println("Sad robot is sad during autonomous :(");
    }
    
    int current = 0;
    private static long startTime;

    /**
     * Do script in autonomous.
     * I originally planned to script autonomous. But, due to a lack of testing time,
     * I scrapped this idea and just went with the usual procedural method of writing
     * autonomous.
     * @param script the autonomous script
     * @deprecated
     */
    public void doScript(double[][] script) {

        if(current < script.length) {
            startTime = System.currentTimeMillis();
            switch((int)script[current][0]) {
                case (int)AutonomousScripts.DRIVE:
                    drive(script[current][1],           //First arg, power
                         (script[current][2] == AutonomousScripts.DISTANCE),   //Second arg, Distance/time select
                          script[current][3],           //Third arg, distance/time
                          script[current][4]);          //Fourth arg, timout
                    break;
                case (int)AutonomousScripts.WAIT:
                    pause((long)script[current][1]);
                    break;
                case (int)AutonomousScripts.TURN:
                    turn(script[current][1], 
                            script[current][2],
                      (long)script[current][4]);
            }
            current++;
        }
    }

    /**
     * Drive. for scripted autonomous.
     * Depreciated due to reasons explained in doScript()'s javadoc comment
     * @param power The speed to drive
     * @param dist Use distance or time to measure.
     * @param distTime The unit to move
     * @param timeOut The timeout. Just in case something breaks
     * @deprecated
     */
    private static void drive(double power, boolean dist, double distTime, double timeOut) {
        if(dist) {
            while(true && !(System.currentTimeMillis() - startTime < timeOut)) {   //TODO: CHANGE BOOLEAN TO ENCODER DISTANCE

            }
        } else {
            while(System.currentTimeMillis() - startTime < distTime && !(System.currentTimeMillis() - startTime < timeOut)) {
                JamesBot.robot.tankDrive(power, power);
            }
            JamesBot.robot.tankDrive(0,0);
        }
    }

    /**
     * Turn. Does nothing. For scripted autonomous
     * Depreciated due to reasons explained in doScript()'s javadoc comment
     * @param power The speed to turn
     * @param angle The angle to turn to
     * @param timeout The timeout. just in case something happens
     * @deprecated
     */
    private static void turn(double power, double angle, long timeout) {

    }

    /**
     * Pause. For scripted autonomous.
     * Depreciated due to reasons explained in doScript()'s javadoc comment
     * @param wait time in milliseconds to wait
     * @deprecated
     */
    private static void pause(long wait) {
        while(System.currentTimeMillis() - startTime < wait) {
            System.out.println("Wait for it...");
        }

    }

    /**
     * Autonomous script stuff.
     * Depreciated due to reasons explained in doScript()'s javadoc comment
     * @deprecated
     */
    public static class AutonomousScripts {
        private static final double DRIVE = 0;  //Drive straight for a determined distance/time
        private static final double TURN  = 1;  //Turn to an angle at a determined angle
        private static final double WAIT  = 2;  //Wait for a determined time
        private static final double FOLLOW= 3;  //Follow the lines
        private static final double ARM_SET=4;  //Set the arm to the setpoint
        private static final double ARM_FOLD=5; //Fold the arm
        private static final double SCORE = 6;  //Score the ubertube
        private static final double END   = 99;

        /* Drive arguments: */
        private static final double DISTANCE = 50;
        private static final double TIME     = 51;

        /*
         * Command:    Arg1:        Arg2:        Arg3:        Arg4:
         * Drive       Power        Dist/TimeSet Dist/TimeVal Timeout
         * Turn        Power        Angle        N/A          Timeout
         * Wait        Time(Millis) N/A          N/A          Timeout
         * Follow      Fork/StraightN/A          N/A          Timeout
         * Arm_Set     Armkickbutt SetPoint N/A          N/A          Timeout
         * End         N/A          N/A          N/A          Timeout
         */

    }
}