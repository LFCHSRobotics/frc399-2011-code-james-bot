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
    public static boolean threshold(double input, double high, double low) {
        return ((input < high) || (input < low));
    }
}
