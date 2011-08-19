/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.Safety;

/**
 *
 * @author Jeremy Germita
 */
public class ArmSafetyThread {

    private boolean fault = false;
    private double current;
    private double position;
    private double speed;

    //Values for monitoring current:
    private final double CURRENT_DANGER_LEVEL = 14.0; //Stall current of the motors is 15A, but I use a lower value for good measure
    private final int CURRENT_DANGER_COUNT = 250;   //Periodic runs at about 50Hz, this is roughly equivalent to 5 seconds

    //Values for monitoring position:
    private double prevPosition;                    //Previous position
    private final int POSITION_DANGER_COUNT = 25;   //About .5 seconds response time if a fault occurs in positioning

    public ArmSafetyThread() {
        System.out.println("[ARM] Safety Thread Initialized.");
        System.out.println("[ARM] Now monitoring current, speed, and position of arm");
    }

    /**
     * Updates all variables to watch. Must be updated in every loop or a fault will occur.
     * @param t_current
     * @param t_position
     * @param t_speed
     */
    public void update(double t_current, double t_position, double t_speed) {
        current = t_current;    //Read values into the variables for monitoring
        position = t_position;
        speed = t_speed;
        fault = watchCurrent() || watchPosition();
    }

    /**
     * Get the fault state
     * @return true if there is a fault
     */
    public boolean getFaultState() {
        return fault;
    }

    private int currCount = 0;
    /**
     * Monitor the current being drawn by the arm.
     * @return returns true if arm is over currenting
     */
    private boolean watchCurrent() {
        boolean currFault;
        if(current > CURRENT_DANGER_LEVEL && speed != 0) {
            currCount++;
        } else {
            currCount = 0;
        }

        currFault = currCount > CURRENT_DANGER_COUNT;

        if(currFault) {
            System.out.println("[ARM] CURRENT FAULT");
        }
        return currFault;
    }

    private int posCount = 0;
    /**
     * Monitor the position of the arm
     * <i>
     * if the arm is being given power(greater than 2.4 volts)
     * <b>AND</b>
     * if the potentiometer value has remained the same for about .5 seconds,
     * return true
     * <b>ALSO</b>
     * if the position value is greater than 4.95 volts or less than 0.05
     * return true
     * </i>
     * @return return true if the position has remained the same for too long.
     */
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
        if(posFault) {
            System.out.println("[ARM] POSITION FAULT");
        }
        return posFault;
    }

}
