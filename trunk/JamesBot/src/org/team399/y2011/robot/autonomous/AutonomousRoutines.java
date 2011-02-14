/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.autonomous;
import org.team399.y2011.robot.JamesBot;

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
    
    
    public void doScript(double[][] script) {

    }
    
    public static class AutonomousScripts {
        private static final double DRIVE = 0;
        public static double[][] trackLines = {
            {DRIVE, 0,0,0,0}
        };
    }
}
