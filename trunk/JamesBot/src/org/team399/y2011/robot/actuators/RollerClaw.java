/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.CANJaguar;

/**
 * Roller claw class
 * @author Jeremy Germita, Jackie Patton
 */
public class RollerClaw {

    private CANJaguar rollerA, rollerB; //The roller CANJaguars

    /**
     * Constructor
     */
    public RollerClaw() {
        try {
            rollerA = new CANJaguar(3); //Create CANJaguar objects on addresses
            rollerB = new CANJaguar(9); //3 and 9
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("ERROR INITIALIZING ROLLER CLAW");
        }
    }

    /**
     * Run the motors simultaneously to pull or push the tubes in/out
     * @param speed the speed to run the motors
     */
    public void grab(double speed) {
        try {
            rollerA.setX(speed);
            rollerB.setX(speed);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the rollers at opposite speeds to articulate the tubes up/down
     * @param speed The speed to run the rollers
     */
    public void articulate(double speed) {
        try {
            rollerA.setX(-speed);
            rollerB.setX(speed);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run a motor at the set speed
     * @param motor 0 for motorA, 1 for motorB
     * @param speed the speed to run said motor
     */
    public void setRaw(int motor, double speed) {
        switch(motor) {
            case 0:
                try {
                    rollerA.setX(speed);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    rollerB.setX(speed);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
