/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;
import edu.wpi.first.wpilibj.Solenoid;
/**
 *
 * @author Jeremy Germita, Jackie Patton
 */
public class PincherClaw {
    
    private Solenoid clawA;   //Double solenoid for pincher claw
    private Solenoid clawB;

    /**
     * Constructor
     * @param portA Port a of the solenoid
     * @param portB Port b of the solenoid
     */
    public PincherClaw(int portA, int portB) {
        clawA = new Solenoid(7,portA);
        clawB = new Solenoid(7,portB);
    }

    boolean shifted = false, gear = false;
    public void grab(boolean shift) {
        if(shift && !shifted) {
            gear = !gear;
            clawA.set(gear);
            clawB.set(!gear);
            shifted = true;
        } else if(!shift) {
            shifted = false;
        }
    }

}