/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.autonomous;
import edu.wpi.first.wpilibj.Timer;
import org.team399.y2011.robot.JamesBot;
import org.team399.y2011.robot.sensors.LineSensorArray;
//  import edu.wpi.first.wpilibj.Timer;             // initialize Timer for Auton. JKG 2/14


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
        System.out.println(lsa.getArrayState());
    }



      /* public static void autonLeftLine() {
            arm.setPoint(Arm.ArmStates.HIGH);
            AutonomousRoutines.LineFollow();
            claw.grab(false);

            JamesBot.robot.tankDrive(-1,-1);
           Timer.delay(2);
           JamesBot.robot.tankDrive(1,1);
           Timer.delay(5);
           JamesBot.robot.tankDrive(0,0);
        }
     * JKG 2/14 12:30pm
     * Set arm to High
     * line follow until the end of the tape
     * Open claw, release tube
     * drive out reverse for 2 seconds, then turn around and drive forward for 5
     */

    /**
     * Spin
     */
    public static void autonTwo() {
        JamesBot.robot.tankDrive(-1, 1);
    }

    private void test() {}

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
                    drive(script[current][1],
                         (script[current][2] == 1.0),
                          script[current][3],
                          script[current][4]);
                    break;
                case (int)AutonomousScripts.WAIT:
                    //wait();
            }
            current++;
        }
    }

    private static void drive(double power, boolean dist, double distTime, double timeOut) {
        while(System.currentTimeMillis() - startTime < timeOut) {
            if(dist) {
                while(true) {   //TODO: CHANGE BOOLEAN TO ENCODER DISTANCE

                }
            } else {
                while(System.currentTimeMillis() - startTime < distTime) {
                    JamesBot.robot.tankDrive(power, power);
                }
                JamesBot.robot.tankDrive(0,0);
            }
        }
        JamesBot.robot.tankDrive(0,0);
    }

    private static void pause(long wait) {
        while(System.currentTimeMillis() - startTime < wait) {

        }

    }
    
    public static class AutonomousScripts {
        private static final double DRIVE = 0;  //Drive straight for a determined distance/time
        private static final double TURN  = 1;  //Turn to an angle at a determined angle
        private static final double WAIT  = 2;  //Wait for a determined time
        private static final double FOLLOW= 3;  //Follow the lines
        private static final double ARM_SET=4;  //Set the arm to the setpoint
        private static final double SCORE = 5;  //Score the ubertube
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