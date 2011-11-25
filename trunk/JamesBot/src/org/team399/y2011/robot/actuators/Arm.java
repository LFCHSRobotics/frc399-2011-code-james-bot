/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Relay;
import org.team399.y2011.robot.utilities.ExceptionHandler;
import org.team399.y2011.robot.Safety.ArmSafetyThread;

/**
 * Arm class
 * @author Jeremy Germita
 */
public class Arm {

    private ArmSafetyThread safety;
    private CANJaguar armA; //Instance of arm CAN Jaguar, A
    private CANJaguar armB; //Instance of arm CAN Jaguar, B
    private Solenoid hingeA;    //The arm hinge solenoids
    private Solenoid hingeB;
    private AnalogChannel pot;          //Instance of the potentiometer
    private Relay fan = new Relay(2);   //Cooling fans
    private DigitalInput zeroSwitch = new DigitalInput(2);  //Bumper switch at the lower limit
    private static double lowerLimit = 3.090399943;
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
            armA.configNeutralMode(CANJaguar.NeutralMode.kCoast); //Brake the motors
            armB.configNeutralMode(CANJaguar.NeutralMode.kCoast); //Brake the motors
            armA.setVoltageRampRate(0);
            armB.setVoltageRampRate(0);
            //armA.setVoltageRampRate(33);
            //armB.setVoltageRampRate(33);
        } catch (Throwable e) {
            System.out.print("[ARM] ERROR INITIALIZING ARM");
            new ExceptionHandler(e, "Arm").print();
        }
        fan.set(Relay.Value.kForward);
        safety = new ArmSafetyThread();
    }

    /**
     * Sets the volt ramp rate for the arm
     * @param rate Ramp rate in v/s
     */
    public void setVoltRampRate(double rate) {
        try {
            armA.setVoltageRampRate(rate);
            armA.setVoltageRampRate(rate);
        } catch (Throwable e) {
            new ExceptionHandler(e, "Arm").print();
        }
    }

    /**
     * Arm state interface
     */
    public interface ArmStates {

        public static double HIGH = lowerLimit - 0.6737747890000003;   //3.203552961
        public static double MID = lowerLimit - 0.45775539100000007;  //3.41442904
        public static double LOW = lowerLimit - 0.10286638000000004;  //3.522438739
        public static double GROUND = lowerLimit - 0.09286638000000004;  //3.7641747320000003
        public static double INSIDE = lowerLimit - 0.05;
        public static double LOWER_LIMIT = lowerLimit;
        //public static double TOMAHAWK_HIGH = lowerLimit - 0.5804602710000001;
    }

    /**
     * Set the arm motors. Upper and lower limits are in place
     * @param value The motor speed
     */
    public void set(double value) {
        try {
            if (enableSoftLimits) {
                armA.setX(-value, (byte) 2); //Set arm motors to value only if it is negative
                armB.setX(value, (byte) 2);
            }
            CANJaguar.updateSyncGroup((byte) 2);    //Update the Sync group
        } catch (Throwable e) {
            new ExceptionHandler(e, "Arm").print();
        }
        if (!zeroSwitch.get()) {
            lowerLimit = pot.getAverageVoltage();
            System.out.println("ZERO!");
        }

    }

    /**
     * Print the potentiometer value
     */
    public void print() {
        System.out.println("[ARM] Position: " + pot.getAverageVoltage());
        //System.out.println("[ARM] Displacement from 0: " + (lowerLimit - pot.getAverageVoltage()));
        //System.out.println("[ARM] Current: " + getCurrent());
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
        enabled = !safety.getFaultState();
        if (!zeroSwitch.get()) {
            lowerLimit = pot.getAverageVoltage();
            if (setpoint > lowerLimit) {
                setpoint = lowerLimit;
            }
            System.out.println("ZERO!");
        }
        try {
            System.out.println("Current: " + (armA.getOutputCurrent() + armB.getOutputCurrent()) / 2);
            processValue = pot.getAverageVoltage();      //processValue is assigned the value of the pot
            error = setpoint - processValue;      //error is the distance between the setpoint and process value
            integral += error;                       //integral is the sum of the current and previous errors
            derivative = error - prevError;              //derivative is the rate of change between the current and prevErrors
            output = (P * error) + //Calculate PID output
                    (I * integral)
                    - (D * derivative);

            if (output > speedLimit) {       //Output limiting
                output = speedLimit;
            } else if (output < -speedLimit) {
                output = -speedLimit;
            }

            if (!zeroSwitch.get()) {
                lowerLimit = pot.getAverageVoltage();
                if (output > 0) {
                    output = 0;
                    setpoint = lowerLimit;
                }
                //System.out.println("ZERO!");
            }
            if (enabled) {
                set(-output);
            }

            prevError = error;                   //prevError is now equal to error
        } catch (Throwable e) {
            new ExceptionHandler(e, "Arm").print();
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
        if (point >= lowerLimit) {
            setpoint = lowerLimit;
        } else {
            if (point > ArmStates.GROUND) {
                //fold
            }
            setpoint = point;
        }
        setpoint = point;
    }

    public void deltaControl(double stickInput) {
        double deltaGain = .015; //Increasing this will increase the speed at which the delta increases. This will increase arm "Laggy-ness"
        if (enabled) {
            setPoint(getSetpoint() + (stickInput * deltaGain));
        } else {
            set(stickInput);
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
            return ((armA.getOutputCurrent() + armB.getOutputCurrent()) / 2);
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }

    /**
     * Get the arm's output voltage
     * @return
     */
    public double getVoltage() {
        try {
            return ((armA.getOutputVoltage() + armA.getOutputVoltage()) / 2);
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }

    /**
     * Bus voltage(battery voltage)
     * @return
     */
    public double getBusVoltage() {
        try {
            return armA.getBusVoltage();
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }

    /**
     * Folds the arm. Operator-friendly toggle version
     * @param shift A button input to fold the arm. Goes true to toggle the arm state
     */
    public void fold(boolean shift) {
        if (shift && !shifted) {
            gear = !gear;
            setElbow(gear);
            shifted = true;
        } else if (!shift) {
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
}
