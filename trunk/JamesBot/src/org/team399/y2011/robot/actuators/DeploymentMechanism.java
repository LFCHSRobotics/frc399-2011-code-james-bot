/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Deployment mechanism class
 * @author Jeremy Germita and Jackie Patton
 */
public class DeploymentMechanism {

    private DoubleSolenoid deployActuator;  //The actuator

    /**
     * Constructor
     * @param a actuator port A
     * @param actuator port b
     */
    public DeploymentMechanism(int a, int b) {
        deployActuator = new DoubleSolenoid(a, b);  //Instantiates the Double solenoid
    }

    /**
     * DEPLOY THE MINIBOT!
     * @param state the boolean input used to deploy
     */
    public void deploy(boolean state) {
        deployActuator.set(((state) ? DoubleSolenoid.Value.kForward :
            DoubleSolenoid.Value.kReverse));    //sets the solenoid
    }
}
