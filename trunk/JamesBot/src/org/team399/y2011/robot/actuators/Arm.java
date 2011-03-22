/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team399.y2011.robot.utilities.ExceptionHandler;

/**
 * Arm class
 * @author Jeremy Germita
 */
public class Arm {
    private static double lowerLimit = 2.40576524;
    private static double upperLimit = lowerLimit - 1.1778200509999999; //1.4599678200000001;
    //TODO:  EDIT ArmState INTERFACE VALUES FOR PID SETPOINTS
    //TODO:  FOR SETPOINTS, USE A RELATIVE SYSTEM. START ARM IN "DOWN" POSITION, EACH SETPOINT IS X FROM DOWN
    /**
     * Arm state interface
     */
    public interface ArmStates {
        public static double HIGH          = lowerLimit - 0.6994913839999999;   //0.6994913839999999
        public static double MID           = lowerLimit - 0.6994913839999999;;  //TODO: CHANGE VALUES
        public static double LOW           = lowerLimit - 0.6994913839999999;;  //Currently, all setpoints are high
        public static double GROUND        = lowerLimit - 0.6994913839999999;;
        public static double INSIDE        = lowerLimit - 0.6994913839999999;;
        public static double TOMAHAWK_HIGH = lowerLimit - 0.6994913839999999;;
    }
    
    private CANJaguar armA; //Instance of arm CAN Jaguar, A
    private CANJaguar armB; //Instance of arm CAN Jaguar, B

    private Solenoid hingeA;
    private Solenoid hingeB;

    private AnalogChannel pot;  //Instance of the potentiometer
    private DigitalInput reedSwitch;

    /**
     * Constructor
     */
    public Arm(int aPort, int bPort) {
        pot = new AnalogChannel(1); //potentiometer on analog 1
        hingeA = new Solenoid(aPort);  //Arm hinge solenoid
        hingeB = new Solenoid(bPort);  //Arm hinge solenoid
        reedSwitch = new DigitalInput(13);  //Reed switch
        try {
            armA = new CANJaguar(4);    //armA is a CANJaguar with the address 6
            armB = new CANJaguar(7);    //armB is a CANJaguar with the address 7
            armA.configNeutralMode(CANJaguar.NeutralMode.kBrake); //Brake the motors
            armB.configNeutralMode(CANJaguar.NeutralMode.kBrake); //Brake the motors
            //armA.setSafetyEnabled(true);    //Enable safety on the arm.
            //armB.setSafetyEnabled(true);    //We don't want to kill anyone. :P
        } catch(Throwable e) {
            System.out.print("ERROR INITIALIZING ARM");
            new ExceptionHandler(e, "Arm").print();
        }
    }


    /**
     * Set the arm motors. Upper and lower limits are in place
     * @param value The motor speed
     */
    public void set(double value) {
        try {
            if(pot.getAverageVoltage() > upperLimit &&      //Upper and lower limit logic
                    pot.getAverageVoltage() < lowerLimit) {
                armA.setX(value, (byte) 2);   //Set armA to the argument, value
                armB.setX(value, (byte) 2);  //Set armB to the argument, value, times -1

            } else if(pot.getAverageVoltage() > upperLimit) {
                armA.setX(((value >= 0) ? value : 0 ) , (byte) 2); //Set arm motors to value only if it is negative
                armB.setX(((value >= 0) ? value : 0 ), (byte) 2);
            } else if(pot.getAverageVoltage() < lowerLimit) {
                armA.setX(((value <= 0) ? value : 0 ) , (byte) 2);   //Set arm motors to value only if it is positive
                armB.setX(((value <= 0) ? value : 0 ), (byte) 2);
            }

            allowFolding = !(getSetpoint() > ArmStates.GROUND); //Automatic folding for the arm

            CANJaguar.updateSyncGroup((byte) 2);    //Update the Sync group
        } catch(Throwable e) {
            new ExceptionHandler(e, "Arm").print();
        }
    }

    private boolean allowFolding = true;

    /**
     * Print the potentiometer value
     */
    public void print() {
        System.out.println("Potentiometer Value: " + pot.getAverageVoltage());
        System.out.println("Setpoint: " + setpoint);
    }

    public boolean getReedSwitch() {
        return !reedSwitch.get();
    }

    private double processValue;    //The potentiometer input
    private final double P = 3, I = 0.0001, D = 0; //P, I, and D values
    private double error, prevError, output, integral, derivative; //Other values needed by PID
    private boolean enabled = true;

    /**
     * Update pid
     */
    public void update() {
        if(enabled) {
            try {
                processValue = pot.getAverageVoltage();      //processValue is assigned the value of the pot
                error        = setpoint - processValue;      //error is the distance between the setpoint and process value
                integral     += error;                       //integral is the sum of the current and previous errors
                derivative   = error-prevError;              //derivative is the rate of change between the current and prevErrors
                output       = (P*error) +                   //Calculate PID output
                               (I*integral) -
                               (D*derivative);
                
                if(output > speedLimit) {       //Output limiting
                    output = speedLimit;
                } else if(output < -speedLimit){
                    output = -speedLimit;
                }
                set(-output);
                prevError = error;                   //prevError is now equal to error
            } catch(Throwable e) {
                new ExceptionHandler(e, "Arm").print();
            }
        }
    }
    private double speedLimit = 1;

    public void setSpeedLimit(double limit) {
        this.speedLimit = limit;
    }
    
    private double setpoint;// = ArmStates.MID;
    /**
     * Set the point for the arm
     * @param point the setpoint
     */
    public void setPoint(double point) {
      if(point <= upperLimit) {
        setpoint = upperLimit;
      } else if(point >= lowerLimit) {
        setpoint = lowerLimit;
      } else {
        setpoint = point;
      }
    }

    /**
     * Enable PID Control for the arm
     */
    public void enable() {
        enabled = true;
    }

    /**
     * Disable pid control
     */
    public void disable() {
        enabled = false;
        integral = 0.0;
        enabled = false;
        set(0.0);
    }

    public void reset() {
        disable();
        enable();
    }

    /**
     * return the setpoint
     * @return the setpoint
     */
    public double getSetpoint() {
        return setpoint;
    }

    /**
     * Print the average current pulled by the arm
     * @return The average arm current
     */
    public double getCurrent() {
        try {
            return ((armA.getOutputCurrent() + armB.getOutputCurrent())/2);
        } catch(Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }


    boolean shifted = false, gear = false;
    public void fold(boolean shift) {
      if(allowFolding) {
            if(shift && !shifted) {
                gear = !gear;
                setElbow(gear);
                shifted = true;
            } else if(!shift) {
                shifted = false;
            }
      } else {
          setElbow(true);
      }
    }

    public void setElbow(boolean state) {
        hingeA.set(state);
        hingeB.set(!state);
    }
    
    public double getPosition() {
        return pot.getAverageValue();
    }
}
