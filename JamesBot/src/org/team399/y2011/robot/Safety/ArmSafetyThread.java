/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.Safety;

/**
 *
 * @author robotics
 */
public class ArmSafetyThread {

    private boolean fault = false;
    private double current;
    private double position;
    private double speed;
    private double temp;

    private final double CURRENT_DANGER_LEVEL = 0.0;
    private final int CURRENT_DANGER_COUNT = 0;
    
    private double prevPosition;
    private final int POSITION_DANGER_COUNT = 0;

    private final double TEMPERATURE_DANGER_LEVEL = 4.0;

    //Note to self: check to see if the rate at which this thread runs is not faster than
    //the speed of the FRC periodic methods. If that is true, then the counters would run
    //faster here than in the main thread.

    /**
     * Updates all variables to watch. Must be updated in every loop or a fault will occur.
     * @param t_current
     * @param t_position
     * @param t_speed
     * @param t_temp
     */
    public void update(double t_current, double t_position, double t_speed, double t_temp) {
        current = t_current;    //Read values into the variables for monitoring
        position = t_position;
        speed = t_speed;
        temp = t_temp;
    }

    public boolean getFault() {
        return fault;
    }

    public void run() {
        fault = watchCurrent() || watchPosition() || watchTemperature();
    }

    private int currCount = 0;

    private boolean watchCurrent() {
        boolean currFault;
        if(current > CURRENT_DANGER_LEVEL) {
            currCount++;
        } else {
            currCount = 0;
        }

        currFault = currCount > CURRENT_DANGER_COUNT;

        return currFault;
    }

    private int posCount = 0;
    private boolean watchPosition() {
        boolean posFault;
        if(Math.abs(speed) > .2 && prevPosition == position) {
            posCount++;
        } else {
            posCount = 0;
        }

        posFault = (posCount > POSITION_DANGER_COUNT) ||
                   (position > 4.95) ||
                   (position < 0.05);
        prevPosition = position;
        return posFault;
    }

    private boolean watchTemperature() {
        return temp > TEMPERATURE_DANGER_LEVEL;
    }
}
