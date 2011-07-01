/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team399.y2011.robot.utilities.ExceptionHandler;

/**
 * Roller claw class
 * @author Jeremy Germita, Jackie Patton
 */
public class RollerClaw {

    private CANJaguar rollerA, rollerB; //The roller CANJaguars
    private DigitalInput limit;

    /**
     * Constructor
     */
    public RollerClaw() {
        try {
            rollerA = new CANJaguar(3); //Create CANJaguar objects on addresses
            rollerB = new CANJaguar(9); //3 and 9
            rollerA.configNeutralMode(CANJaguar.NeutralMode.kCoast);
            rollerB.configNeutralMode(CANJaguar.NeutralMode.kCoast);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("ERROR INITIALIZING ROLLER CLAW");
        }

        limit = new DigitalInput(1);

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
     * Get the state of the limit switches on the claw
     * @return Limit switch state
     */
    public boolean getLimitSwitch() {
        return !limit.get();
    }

    /**
     * Automatic hold tube method
     */
    public void holdTube() {
        if(!getLimitSwitch()) {
            grab(1);
        } else {
            grab(0);
        }
    }

    /**
     * Automatic spit tube method
     */
    public void spitTube() {
        if(getLimitSwitch()) {
            grab(-1);
        } else {
            grab(-.5);
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

    /**
     * Get the output current of motor A
     * @return Current
     */
    public double getCurrentA() {
        try {
            return rollerA.getOutputCurrent();
        } catch (Exception e) {
            new ExceptionHandler(e, "Roller Claw").print();
            return -0;
        }
    }

    /**
     * Get the output current of motor b
     * @return Current
     */
    public double getCurrentB() {
        try {
            return rollerB.getOutputCurrent();
        } catch (Exception e) {
            new ExceptionHandler(e, "Roller Claw").print();
            return -0;
        }
    }

    public static class CURRENT_LEVELS {
        public static class UPPER_MOTOR {

        }
        public static class LOWER_MOTOR {
            
        }
    }

}