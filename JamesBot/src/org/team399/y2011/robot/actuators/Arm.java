/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import org.team399.y2011.robot.utilities.ExceptionHandler;

/**
 * Arm class
 * @author Jeremy Germita
 */
public class Arm {
    //TODO:  EDIT ArmState INTERFACE VALUES FOR PID SETPOINTS
    /**
     * Arm state interface
     */
    public interface ArmStates {
        /**
        public static final double kOff_val = 0;
        public static final double kForward_val = 1;
        public static final double kReverse_val = 2;*/
    }
    
    private CANJaguar armA; //Instance of arm CAN Jaguar, A
    private CANJaguar armB; //Instance of arm CAN Jaguar, B

    private AnalogChannel pot;  //Instance of the potentiometer

    /**
     * Constructor
     */
    public Arm() {
        pot = new AnalogChannel(1); //potentiometer
        try {
            armA = new CANJaguar(4);    //armA is a CANJaguar with the address 6
            armB = new CANJaguar(7);    //armB is a CANJaguar with the address 7
            armA.configNeutralMode(CANJaguar.NeutralMode.kBrake); //Brake the motors
            armB.configNeutralMode(CANJaguar.NeutralMode.kBrake);
        } catch(Throwable e) {
            System.out.print("ERROR INITIALIZING ARM");
            new ExceptionHandler(e, "Arm").print();
        }
    }

    private double upperLimit = 1.12;
    private double lowerLimit = 1.9;

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
            CANJaguar.updateSyncGroup((byte) 2);    //Update the Sync group
        } catch(Throwable e) {
            new ExceptionHandler(e, "Arm").print();
        }
    }

    /**
     * Print the potentiometer value
     */
    public void print() {
        System.out.println(pot.getAverageVoltage());
    }

    private double processValue;    //The potentiometer input
    private final double P = 10, I = 0, D = 0; //P, I, and D values
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
                if(output > 1.0) {
                    output = 1.0;
                } else if(output < -1.0){
                    output = -1.0;
                }
                System.out.println("O/P" + output);
                set(-output);
                prevError = error;                   //prevError is now equal to error
            } catch(Throwable e) {
                new ExceptionHandler(e, "Arm").print();
            }
        }
    }
    
    private double setpoint;
    /**
     * Set the point for the arm
     * @param point the setpoint
     */
    public void setPoint(double point) {
      if(point >= upperLimit) {
        setpoint = upperLimit;
      } else if(point <= lowerLimit) {
        setpoint = lowerLimit;
      } else {
        setpoint = point;
      }
    }

    /**
     * Enable PID Control for the arm
     */
    public void enable() {
        //TODO: Implement integral zeroing
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

}
