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
public class CamPan {

    Servo CamPan = new Servo(1);
    boolean front = true;           // start facing forward

    // front == true - Camera is facing towards claw
    // front == false - Camera is facing the back
    public void flipCam(boolean change) {

        if(front == true) {
            CamPan.set(1);
            front = false;
        } else {
            CamPan.set(0);
            front = true;
        }
    }


}
