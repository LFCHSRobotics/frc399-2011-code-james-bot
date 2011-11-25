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
    private long startTime = 0;             //StartTime for auto deploy timer
    private long deployTime = 110000;       //Time to deploy minibot automatically

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
        deployActuator.set(((state) ? DoubleSolenoid.Value.kForward
                : DoubleSolenoid.Value.kReverse));    //sets the solenoid
    }

    /**
     * Begin automatic deployment timer
     */
    public void beginTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Deploy the minibot with a timer
     */
    public void timerDeploy() {
        if (getTimeSinceStart() > deployTime) {
            deploy(true);
        } else {
            deploy(false);
        }
    }

    /**
     * Get the current time on the clock
     * @return the time returned by the system
     */
    public long getTimer() {
        return System.currentTimeMillis();
    }

    /**
     * get the time since beginTimer was last called
     * @return the time since beginTimer was last called
     */
    public long getTimeSinceStart() {
        return getTimer() - startTime;
    }
}
