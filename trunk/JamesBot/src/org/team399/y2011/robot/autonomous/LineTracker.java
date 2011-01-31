/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.autonomous;
import org.team399.y2011.robot.sensors.LineSensorArray;

/**
 *
 * @author Jeremy Germita
 */
public class LineTracker {
    LineSensorArray lsa = new LineSensorArray(1,2,3);

    public LineTracker() {

    }
    public void track() {
        System.out.println(lsa.getArrayState());
    }
}
