/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Solenoid;
import org.team399.y2011.robot.utilities.ExceptionHandler;

/**
 * Arm class
 * @author Jeremy Germita
 */
public class Arm {
    private static double lowerLimit = 3.795034646;
    
    private CANJaguar armA; //Instance of arm CAN Jaguar, A
    private CANJaguar armB; //Instance of arm CAN Jaguar, B

    private Solenoid hingeA;    //The arm hinge solenoids
    private Solenoid hingeB;

    private AnalogChannel pot;          //Instance of the potentiometer

    //PID Variables:************************************************************
    private double processValue;                   //The potentiometer input
    private final double P = 9, I = .09, D = 0; //P, I, and D values
    private double error, prevError, output, integral, derivative; //Other values needed by PID
    private boolean enabled = true;                //Enables/Disables closed loop PID
    private double speedLimit = 1;                 //Speed limit for arm. Defaults to 1(full power)
    private double setpoint;                       //The current setpoint for the arm

    boolean shifted = false, gear = false;         //For the folding solenoid
    boolean enableSoftLimits = true;               //Enables/disables software limits

    /**
     * Constructor
     */
    public Arm(int aPort, int bPort) {
        pot = new AnalogChannel(1); //potentiometer on analog 1
        hingeA = new Solenoid(aPort);  //Arm hinge solenoid
        hingeB = new Solenoid(bPort);  //Arm hinge solenoid
        try {
            armA = new CANJaguar(4);    //armA is a CANJaguar with the address 6
            armB = new CANJaguar(7);    //armB is a CANJaguar with the address 7
            armA.configNeutralMode(CANJaguar.NeutralMode.kBrake); //Brake the motors
            armB.configNeutralMode(CANJaguar.NeutralMode.kBrake); //Brake the motors
        } catch(Throwable e) {
            System.out.print("ERROR INITIALIZING ARM");
            new ExceptionHandler(e, "Arm").print();
        }


    }

    /**
     * Arm state interface
     */
    public interface ArmStates {
        public static double HIGH          = lowerLimit - 0.6320549609999998;   //3.203552961
        public static double MID           = lowerLimit - 0.452612072;  //3.41442904
        public static double LOW           = lowerLimit - 0.344602373;  //3.522438739
        public static double GROUND        = lowerLimit - 0.1028663799999997;  //3.7641747320000003
        public static double INSIDE        = lowerLimit;
        public static double TOMAHAWK_HIGH = lowerLimit - 0.5804602710000001;
    }

    /**
     * Set the arm motors. Upper and lower limits are in place
     * @param value The motor speed
     */
    public void set(double value) {
        try {
            if(enableSoftLimits) {
                armA.setX(-value , (byte) 2); //Set arm motors to value only if it is negative
                armB.setX(value, (byte) 2);
            }

            CANJaguar.updateSyncGroup((byte) 2);    //Update the Sync group
        } catch(Throwable e) {
            new ExceptionHandler(e, "Arm").print();
        }

        System.out.println("Arm Power:" + value);
        
    }

    /**
     * Print the potentiometer value
     */
    public void print() {
        System.out.println("Potentiometer Value: " + pot.getAverageVoltage());
    }

    /**
     * Zero the potentiometer
     */
    public void zeroPotentiometer() {
        lowerLimit = pot.getAverageVoltage();
    }

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

    /**
     * Set the maximum speed for the arm.
     * @param limit speed limit. from 0-1
     */
    public void setSpeedLimit(double limit) {
        this.speedLimit = limit;
    }
    
    /**
     * Set the point for the arm
     * @param point the setpoint
     */
    public void setPoint(double point) {
      if(point >= lowerLimit) {
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

    /**
     * Disables, then re-enables the PID controller
     */
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

    /**
     * Folds the arm. Operator-friendly toggle version
     * @param shift A button input to fold the arm. Goes true to toggle the arm state
     */
    public void fold(boolean shift) {
        if(shift && !shifted) {
            gear = !gear;
            setElbow(gear);
            shifted = true;
        } else if(!shift) {
            shifted = false;
        }
    }

    /**
     * Folds the arm. Autonomous-friendly raw version
     * @param state The state to set the folding solenoids. True == folded, False == extended
     */
    public void setElbow(boolean state) {
        hingeA.set(state);
        hingeB.set(!state);
    }

    /**
     * Get the position from the potentiometer
     * @return The potentiometer's value
     */
    public double getPosition() {
        return pot.getAverageValue();
    }

    /**
     * Put the arm into safeMode
     * @param armSpeed Raw speed to send the arm
     */
    public void safeMode(double armSpeed) {
        set(armSpeed);              //Sets the arm speed to the the argument
    }

    /**
     * Stop and zero the arm potentiometer value
     */
    public void zero() {
        disable();
        lowerLimit = getPosition();
        enable();
    }
}
