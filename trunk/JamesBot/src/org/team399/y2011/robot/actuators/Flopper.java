/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * Deployment alignment system
 * @author Jeremy Germita
 */
public class Flopper {

    private Solenoid flopperA, flopperB;   //Double solenoid for pincher claw

    /**
     * Constructor
     * @param portA Port a of the solenoid
     * @param portB Port b of the solenoid
     */
    public Flopper(int portA, int portB) {
        flopperA = new Solenoid(portA);
        flopperB = new Solenoid(portB);
    }
    boolean flopped = false, position = false;

    /**
     * Move the flopper
     * @param down Button input
     */
    public void flop(boolean down) {
        if (down && !flopped) {
            position = !position;
            flopperA.set(position);
            flopperB.set(!position);
            flopped = true;
        } else if (!down) {
            flopped = false;
        }
    }
}
