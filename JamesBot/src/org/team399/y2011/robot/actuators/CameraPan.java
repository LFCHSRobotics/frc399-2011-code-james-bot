/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;
import  edu.wpi.first.wpilibj.Servo;

/**
 *
 * @author John Graham and Jeremy Germita
 */
public class CameraPan {

    Servo camPan;
    boolean front = true;           // start facing forward

    /**
     * Constructor
     * @param port The servo port
     */
    public CameraPan(int port) {
        camPan = new Servo(port);
    }

    /**
     * Flip the camera
     * @param change The boolean input used to change the camera's state
     */
    public void flipCam(boolean change) {
        if(front && change) {
            set(1);
            front = false;
        } else if(!change){
            set(0);
            front = true;
        }
    }

    /**
     * Set the raw value of the servo
     * @param value The value to set
     */
    public void set(double value) {
        camPan.set(value);
    }
}
