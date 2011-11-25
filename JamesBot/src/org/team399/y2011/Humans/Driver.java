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
 * Driver wrapper class. Specify a driver and the code will load that driver's preferred drive code.
 * @author Jeremy Germita
 */
public class Driver {

    private String who = "A Stranger...";

    /**
     * Constructor
     * @param who Who is driving?
     */
    public Driver(String who) {
        this.who = who;     //Specify the driver
        System.out.println(who + " is driving!");
    }

    /**
     * Initialize the driver's mechanism
     */
    public void init() {
        if (who.equals("Jeremy")) {
            initJeremy();   //Init jeremy's driver profile
        } else {
            initJeremy();   //Init jeremy's driver profile
        }
    }

    /**
     * Drive robot
     */
    public void drive() {
        if (who.equals("Jeremy")) {
            driveJeremy();      //Run Jeremy's drive control code
        } else {
            driveJeremy();
        }
    }

    public void initJeremy() {
        JamesBot.robot.tankDrive(0, 0);  //Stop drive
        JamesBot.robot.highGear();      //start in high gear
    }

    public void driveJeremy() {
        if (JamesBot.rightJoy.getButton(3) || JamesBot.rightJoy.getButton(2)) {  //Arcade drive with cubic turning control
            JamesBot.robot.arcadeDrive(-JamesBot.rightJoy.getY(),
                    -(JamesBot.leftJoy.getX() * JamesBot.leftJoy.getX() * JamesBot.leftJoy.getX()));

        } else {    //Straight tank drive
            JamesBot.robot.tankDrive(JamesBot.leftJoy.getY(),
                    -JamesBot.rightJoy.getY());   //Tank drive, two sticks
        }

        if ((JamesBot.leftJoy.getTrigger() || JamesBot.rightJoy.getTrigger())) { //Hold triggers for low gear
            JamesBot.robot.lowGear();
        } else {                                                                //Otherwise, just stay in high gear
            JamesBot.robot.highGear();
        }
        if (JamesBot.leftJoy.getButton(2) || JamesBot.leftJoy.getButton(3)) {    //Press this button to stow the arm.
            JamesBot.arm.setPoint(Arm.ArmStates.INSIDE);
        }
    }
}
