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

    private Servo m_Servo;
    private Ultrasonic m_Us;


    /**
     * Constructor
     * @param ping The pin attached to the ping input on the sensor
     * @param echo The pin attached to the echo output on the sensor
     * @param servo The servo PWM port
     */
    public UltrasonicPan(int ping, int echo, int servo) {
        this.m_Servo = new Servo(servo);
        this.m_Us = new Ultrasonic(ping, echo);
    }

    /**
     * Set the angle ofthe servo
     * @param angle The angle
     */
    public void setAngle(double angle) {
        this.m_Servo.setAngle(angle);
    }

    /**
     * Pan the servo back and forth
     * @param leftLimit The leftmost limit of the servo
     * @param rightLimit The rightmost limit of the servo
     * @param resolution The resolution of the pan
     */
    public void pan(double leftLimit, double rightLimit, double resolution) {
        for(double i = leftLimit; i < rightLimit; i += resolution) {
            setAngle(i);
        }
        for(double i = rightLimit; i < leftLimit; i -= resolution) {
            setAngle(i);
        }
    }

    /**
     * Get the range in inches from the sensor
     * @return The range in inches
     */
    public double getRange() {
        return this.m_Us.getRangeInches();
    }
}
