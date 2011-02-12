/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.autonomous;
import org.team399.y2011.robot.JamesBot;
import org.team399.y2011.robot.sensors.LineSensorArray;

/**
 * Line Follower program
 * @author Jeremy Germita, Gabe Ruiz, Brad Hall
 */
public class LineFollow {

    private static LineSensorArray lsa = new LineSensorArray(1,2,3);

    public static void followLine(double speed) {

        if(lsa.getArrayState() == 2) {

        }

    }
}
