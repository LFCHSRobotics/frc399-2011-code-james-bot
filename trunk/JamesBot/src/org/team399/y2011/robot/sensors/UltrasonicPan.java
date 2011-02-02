/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.sensors;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Ultrasonic;

/**
 * Ultrasonic sensor mounted on a panning servo
 * @author Jeremy Germita
 */
public class UltrasonicPan {

    private Servo m_Servo;  //The servo
    private Ultrasonic m_Us;//The ultrasonic sensor

    /**
     * Constructor
     * @param ping The pin attached to the ping input on the sensor
     * @param echo The pin attached to the echo output on the sensor
     * @param servo The servo PWM port
     */
    public UltrasonicPan(int ping, int echo, int servo) {
        this.m_Servo = new Servo(servo);        //Servo is a new servo on specified port
        this.m_Us = new Ultrasonic(ping, echo); //Instantiate the sensor on these ports
    }

    /**
     * Set the angle of the servo
     * @param angle The angle
     */
    public void setAngle(double angle) {
        this.m_Servo.setAngle(angle);   //Set the servo angle
    }

    /**
     * Pan the servo back and forth
     * @param leftLimit The leftmost limit of the servo
     * @param rightLimit The rightmost limit of the servo
     * @param resolution The resolution of the pan
     */
    public void pan(double leftLimit, double rightLimit, double resolution) {
        for(double i = leftLimit; i < rightLimit; i += resolution) {
            setAngle(i);    //Pan clockwise
        }
        for(double i = rightLimit; i < leftLimit; i -= resolution) {
            setAngle(i);    //Pan counterclockwise
        }
    }

    /**
     * Get the range in inches from the sensor
     * @return The range in inches
     */
    public double getRange() {
        return this.m_Us.getRangeInches();  //Get the range of the sensor
    }
}
