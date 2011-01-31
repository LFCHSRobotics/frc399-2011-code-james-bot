/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.sensors;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author kspoelstra
 */
public class LineSensorArray {
    int A, B, C;
    DigitalInput[] sensorArray;
    public LineSensorArray(int portA, int portB, int portC) {
        A = portA;
        B = portB;
        C = portC;

        sensorArray[0] = new DigitalInput(A);
        sensorArray[1] = new DigitalInput(B);
        sensorArray[2] = new DigitalInput(C);
    }

    public boolean getRawSensor(int index) {
        return sensorArray[index].get();
    }

    public int getArrayState() {
        return (sensorArray[0].get() ? 1 : 0) +
               (sensorArray[1].get() ? 2 : 0) +
               (sensorArray[2].get() ? 4 : 0);
    }

    public static class kSensors {
        final int SENSOR_A = 0;
        final int SENSOR_B = 1;
        final int SENSOR_C = 2;
    }
}