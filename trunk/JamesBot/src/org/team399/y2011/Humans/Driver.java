/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.Humans;

import org.team399.y2011.robot.JamesBot;

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
    }

    /**
     * Drive robot
     */
    public void drive() {
        if(!JamesBot.rightJoy.getButton(3)) {
            JamesBot.robot.tankDrive(JamesBot.leftJoy.getY(),
                            -JamesBot.rightJoy.getY());   //Tank drive, two sticks
        } else {
            JamesBot.robot.arcadeDrive(JamesBot.leftJoy.getX(), JamesBot.leftJoy.getY()); //Arcade Drive
        }
        JamesBot.robot.shift(JamesBot.rightJoy.getTrigger() || JamesBot.leftJoy.getTrigger()); //Shift the drivetrain()

    }
}
