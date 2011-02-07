/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.Solenoid;
/**
 *
 * @author Jeremy Germita and Jackie Patton
 */
public class DeploymentMechanism {

    private Solenoid DeployA = new Solenoid(3);     //Solenoid on port 3
    private Solenoid DeployB = new Solenoid(4);     //Solenoid on port 4

    public void deploy (boolean state){
    DeployA.set(state);                 //sets to boolean state
    DeployB.set(!state);                //sets to opposite boolean state
    }
}
