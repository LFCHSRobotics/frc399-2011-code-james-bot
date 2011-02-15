/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.autonomous;
import org.team399.y2011.robot.JamesBot;
//  import edu.wpi.first.wpilibj.Timer;             // initialize Timer for Auton. JKG 2/14


/**
 *
 * @author Jeremy Germita and Robert Wong
 */
public class AutonomousRoutines {

    private static String kSide;
    
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

    /**
     * Do nothing in autonomous mode
     */
    public static void disabled() {
        System.out.println("Sad robot is sad during autonomous :(");
    }
    
    int current = 0;

    public void doScript(double[][] script) {

        if(current < script.length) {



            current++;
        }
    }
    
    public static class AutonomousScripts {

        private static final double DRIVE = 0;
        private static final double TURN  = 1;
        private static final double WAIT  = 2;
        private static final double FOLLOW= 3;

        private static final double END   = 99;


        public static double[][] trackLines = {
            {DRIVE, 0,0,0,0}
        };
    }
}
