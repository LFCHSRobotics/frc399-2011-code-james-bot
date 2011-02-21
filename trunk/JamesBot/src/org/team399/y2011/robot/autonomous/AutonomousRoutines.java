/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.autonomous;
import org.team399.y2011.robot.JamesBot;
import org.team399.y2011.robot.actuators.Arm;
import org.team399.y2011.robot.sensors.LineSensorArray;


/**
 *
 * @author Jeremy Germita and Robert Wong
 */
public class AutonomousRoutines {

    private static String kSide;
    private static LineSensorArray lsa = new LineSensorArray(1,2,3);
    
    /**
     * Set the side of the field
     * @param side The side
     */
    public static void setSide(String side) {
        kSide = side;
    }
    /**
     * Track Lines.
     */
    public static void autonOne() {
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < 7000) {
            while(System.currentTimeMillis() - startTime < 4000) {
                if(lsa.getA()) {
                    JamesBot.robot.tankDrive(-.5, .5);
                } else {
                    //Check to see if the drivetrain drifts to the right/left, adjust
                    JamesBot.robot.tankDrive(-.5, .3);
                }
            }
            JamesBot.robot.tankDrive(0, 0);

            JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
            
            //while(Math.abs(JamesBot.arm.getPosition() - JamesBot.arm.getSetpoint()) < .06) {
                //JamesBot.arm.update();
            //}

        }
    }

    /**
     * Spin
     */
    public static void autonTwo() {
        JamesBot.robot.tankDrive(-1, 1);
    }

    /**
     * Do nothing in autonomous mode
     */
    public static void disabled() {
        System.out.println("Sad robot is sad during autonomous :(");
    }
    
    int current = 0;
    private static long startTime;

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

    private static void turn(double power, double ange, long timeout) {

    }

    private static void pause(long wait) {
        while(System.currentTimeMillis() - startTime < wait) {
            System.out.println("Wait for it...");
        }

    }
    
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
         * Arm_Set     Arm SetPoint N/A          N/A          Timeout
         * End         N/A          N/A          N/A          Timeout
         */

    }
}