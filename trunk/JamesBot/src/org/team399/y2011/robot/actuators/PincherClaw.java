/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;
import edu.wpi.first.wpilibj.DoubleSolenoid;
/**
 *
 * @author Jeremy Germita, Jackie Patton
 */
public class PincherClaw {
    
    private DoubleSolenoid claw;   //Double solenoid for pincher claw

    /**
     * Constructor
     * @param portA Port a of the solenoid
     * @param portB Port b of the solenoid
     */
    public PincherClaw(int portA, int portB) {
        claw = new DoubleSolenoid(8, portA, portB);
    }
    private boolean prevState, set;
    /**
     *
     * @param state
     */
    public void grab(boolean state) {
        set = state;
        
            DoubleSolenoid.Value setValue = (set) ? DoubleSolenoid.Value.kForward
                    : DoubleSolenoid.Value.kReverse;    //The value to set the pincher claw
            claw.set(setValue);               //Sets the pincher claw to value
        
    }

    /**
     *
     */
    public static class states {
        /**
         * The closed state
         */
        boolean closed = false;
        /**
         * The opened state
         */
        boolean opened = true;
    }

}
