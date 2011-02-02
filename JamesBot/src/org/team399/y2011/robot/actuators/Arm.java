/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.CANJaguar;

/**
 *
 * @author Jeremy Germita
 */
public class Arm {
    private CANJaguar armA;
    private CANJaguar armB;
    
    public Arm() {
        try {
            armA = new CANJaguar(6);
            armB = new CANJaguar(7);
            armA.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void set(double value) {
        try {
            armA.setX(value);
            armB.setX(-value);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private double processValue;
    private double P = 0, I = 0, D = 0;
    private double error, prevError, output, integral, derivative;

    public void setpoint(double point) {    
        try {
            processValue = armA.getPosition();
            error        = point - processValue;
            integral     = prevError + error;
            derivative   = prevError - error;
            output       = (P*error) +
                           (I*integral) +
                           (D*derivative);
            prevError    = error;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
