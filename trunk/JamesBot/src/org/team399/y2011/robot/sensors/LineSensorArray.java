/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.sensors;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Light sensor array class
 * @author Jeremy Germita
 */
public class LineSensorArray {
    private int A, B, C;                //The light sensor ports
    private DigitalInput sensorA, sensorB, sensorC; //sensor array

    /**
     * Constructor
     * @param portA The left sensor port
     * @param portB Center sensor port
     * @param portC Right sensor port
     */
    public LineSensorArray(int portA, int portB, int portC) {
        A = portA;  //A, B, C are assigned to the the arguments
        B = portB;
        C = portC;

        sensorA = new DigitalInput(A);   //Sensor array, index 0
        sensorB = new DigitalInput(B);   //Sensor array, index 1
        sensorC = new DigitalInput(C);   //Sensor array, index 2
    }

    /**
     * Return the value from a single sensor
     * @param index The sensor to poll. 0-2
     * @return The state of said sensor
     * @deprecated
     */
    public boolean getRawSensor(int index) {
        return false;//sensorArray[index].get();    //Get the value from the sensor
    }

    /**
     * Get the state of the entire array
     * @return  Return the state in decimal(converted from binary)
     */
    public int getArrayState() {
        return (sensorA.get() ? 1 : 0) +
               (sensorB.get() ? 2 : 0) +
               (sensorC.get() ? 4 : 0);
    }
    public boolean getA() {
        return sensorA.get();
    }

    public boolean getB() {
        return sensorB.get();
    }

    public boolean getC() {
        return sensorC.get();
    }

    /**
     * The sensors
     */
    public static class kSensors {
        final int SENSOR_A = 0;
        final int SENSOR_B = 1;
        final int SENSOR_C = 2;
    }
}