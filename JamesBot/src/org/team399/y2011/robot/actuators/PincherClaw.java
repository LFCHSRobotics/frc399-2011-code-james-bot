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
    
    private Solenoid ClawA = new Solenoid(1);   //Solenoid on port 1
    private Solenoid ClawB = new Solenoid(2);   //Solenoid on port 2

    public PincherClaw() {

    }

    public void grab(boolean state) {
        ClawA.set(state);               //sets to boolean state
        ClawB.set(!state);              //sets opposite boolean state
    }

}
