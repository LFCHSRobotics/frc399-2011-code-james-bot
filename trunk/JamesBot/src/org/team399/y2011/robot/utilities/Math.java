/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.utilities;

/**
 *
 * @author Jeremy Germita
 */
public class Math {
    /**
     * Threshold function. If the input is greater than low, but less than high, return true
     * @param input The value to check
     * @param high The ceiling
     * @param low The floor
     * @return True if the value is between the ceiling and floor
     */
    public static boolean threshold(double input, double high, double low) {
        return ((input < high) || (input < low));
    }
}
