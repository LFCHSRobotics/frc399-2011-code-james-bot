/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.utilities;

/**
 * General PID Controller class
 * @author Jeremy Germita
 */
public class PIDController {
    private double P;
    private double I;
    private double D;
    private double error, prevError;

    public PIDController(double P, double I, double D) {
        this.P = P;
        this.I = I;
        this.D = D;
    }

    //public void

}
