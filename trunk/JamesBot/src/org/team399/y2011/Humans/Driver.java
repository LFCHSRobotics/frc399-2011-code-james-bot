/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.Humans;

import com.sun.squawk.util.MathUtils;
import org.team399.y2011.robot.JamesBot;
import java.lang.Math;
import org.team399.y2011.robot.actuators.Arm;

/**
 * Driver object. Used to clean up main teleop loop.
 * @author Jeremy Germita
 */
public class Driver {

    /**
     * Constructor
     * @param who Who is driving?
     */
    public Driver(String who) {
        System.out.println(who + " is driving!");
    }

    /**
     * Initialize the driver's mechanism
     */
    public void init() {
        JamesBot.robot.tankDrive(0,0);
        JamesBot.robot.highGear();
    }

    /**
     * Drive robot
     */
    public void drive() {
        if(JamesBot.rightJoy.getButton(3)) {
            JamesBot.robot.arcadeDrive(-JamesBot.rightJoy.getY(), -(JamesBot.rightJoy.getX()*JamesBot.rightJoy.getX()*JamesBot.rightJoy.getX())); //Arcade Drive
        }  else {
            JamesBot.robot.tankDrive(JamesBot.leftJoy.getY(),
                            -JamesBot.rightJoy.getY());   //Tank drive, two sticks
        }
        JamesBot.robot.shift(JamesBot.leftJoy.getTrigger() || JamesBot.rightJoy.getTrigger()); //Shift the drivetrain()
        if(JamesBot.leftJoy.getButton(2)) {
            JamesBot.arm.setPoint(Arm.ArmStates.INSIDE);
        }
        
    }

}
