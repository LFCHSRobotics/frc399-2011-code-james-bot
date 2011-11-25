/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Encoder;
import org.team399.y2011.robot.utilities.ExceptionHandler;
import org.team399.y2011.robot.utilities.Math;

/**
 *  DriveTrain Class. A wrapper class for CANJaguars.
 * @author Jeremy Germita and Gabe Ruiz
 */
public class DriveTrain {

    private CANJaguar leftA;    //Left motor A
    private CANJaguar leftB;    //Left motor B
    private CANJaguar rightA;    //Right motor A
    private CANJaguar rightB;    //Right Motor B
    private Solenoid shiftA = new Solenoid(7);      //Shifter solenoids
    private Solenoid shiftB = new Solenoid(8);
    private Encoder encoder = new Encoder(11, 10);   //drive Encoder
    private Gyro yaw = new Gyro(2);         //Gyro

    /**
     * Constructor
     */
    public DriveTrain() {
        encoder.start();

        yaw.reset();
        try {
            leftA = new CANJaguar(6);       //Left motor A
            leftB = new CANJaguar(8);       //Left motor B
            rightA = new CANJaguar(2);      //Right motor A
            rightB = new CANJaguar(5);      //Right Motor B
            leftA.configFaultTime(.5);      //change fault time to half a second
            leftB.configFaultTime(.5);
            rightA.configFaultTime(.5);
            rightB.configFaultTime(.5);
        } catch (Throwable e) {
            new ExceptionHandler(e, "DriveTrain").print();
            System.out.println("ERROR INITIALIZING DRIVETRAIN");
            try {
                leftA.enableControl();
                leftB.enableControl();
                rightA.enableControl();
                rightB.enableControl();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * Reset the drivetrain encoder
     */
    public void resetEncoder() {
        encoder.reset();
    }

    /**
     * Get the raw encoder count
     * @return the encoder count
     */
    public double getEncoderCounts() {
        return encoder.getRaw();
    }

    /**
     * Stop encoder counter
     */
    public void stopEncoder() {
        encoder.stop();
    }

    /**
     * start encoder counter
     */
    public void startEncoder() {
        encoder.start();
    }

    /**
     * Reset gyro integrator
     */
    public void resetGyro() {
        yaw.reset();
    }

    /**
     * Get the angle from the gyro
     * @return
     */
    public double getAngleWraparound() {
        double angle = yaw.getAngle();
        if (angle < 0.0) {
            angle += 360;
        } else if (angle > 360.0) {
            angle -= 360;
        }
        return angle;
    }

    /**
     * Get gyro raw angle
     * @return
     */
    public double getAngle() {
        return yaw.getAngle();
    }

    /**
     * PRint gyro angle
     */
    public void print() {
        System.out.println("Angle: " + getAngle());
    }


    /**
     * Automatic shifting code.
     * NOTE
     * The concept is similar to this, but this implementation is terribly, terribly incorrect.
     *
     * Theory for a much better version:
     *  To shift from low gear to high gear:
     *  -Check the encoder and current sensors. compares those values to the motors' max efficiency at that gearing
     *  To shift from High to low:
     *  -Check the encoder, current, and voltage. compare those to the motors' max efficiency at that voltage
     *  -Shift if it cannot reach that efficiency.
     *  use filters on current readings to ensure smooth response
     *
     */
    public void autoShift() {
        if (getAverageCurrent() > 80) {
            shift(true);
        } else {
            shift(true);
        }
    }

    /**
     * Tank Drive Method
     * @param leftV left output
     * @param rightV right output
     */
    public void tankDrive(double leftV, double rightV) {
        try {

            leftA.setX(leftV, (byte) 1);       //Set left motor A
            leftB.setX(leftV, (byte) 1);       //Set left motor B
            rightA.setX(rightV, (byte) 1);     //Set right motor A
            rightB.setX(rightV, (byte) 1);     //Set right motor B
            CANJaguar.updateSyncGroup((byte) 1);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Arcade Drive method
     * @param throttle
     * @param turn
     */
    public void arcadeDrive(double throttle, double turn) {
        tankDrive(-throttle + turn, -(-throttle - turn));
    }
    //PD Values
    private double error = 0, prevError = 0;

    /**
     * Drive to a specified angle. 
     * @param throttle Throttle to drive forward
     * @param angle angle to drive to
     */
    public void driveToAngle(double throttle, double angle) {
        double P = -.03,
                D = .1;
        error = angle - getAngle();
        double proportional = P * error;
        double derivative = error - prevError;
        double PID_Out = proportional - D * derivative;
        prevError = error;
        tankDrive((-throttle) + PID_Out, (throttle) + PID_Out);
    }

    /**
     *  old drive straight code
     * @param throttle Throttle to drive straight
     * @param angle Use 0 to drive short
     */
    public void driveStraight(double throttle, double angle) {
        final double P = .15; // the proportional scaler
        final double D = .05;

        error = angle - yaw.getAngle();
        double outputRight = throttle - ((P * error) - (D * (error - prevError)));
        if (outputRight > 1) {   //this limits our output to the motors to -1/1
            outputRight = 1;
        } else if (-outputRight < -1) {
            outputRight = -1;
        }
        System.out.println("Output: " + outputRight);
        prevError = error;
        tankDrive(-throttle, outputRight);
    }

    /**
     * Set the gyro for use with various drivetrain actions
     * @param gyro
     */
    public void setGyro(Gyro gyro) {
        this.yaw = gyro;
    }

    /**
     * Stay in low gear
     */
    public void lowGear() {
        shiftA.set(false);
        shiftB.set(true);
    }

    /**
     * Stay in high gear
     */
    public void highGear() {
        shiftA.set(true);
        shiftB.set(false);
    }

    /**
     * Get Current at a single motor
     * @param motor The motor to get current from. kLeftA, kLeftB, kRightA, kRightB
     * @return The current in amps
     */
    public double getSingleMotorCurrent(int motor) {
        switch (motor) {
            case kMotors.kLeftA:
                try {
                    return leftA.getOutputCurrent();
                } //Return the current from motor leftA
                catch (Throwable e) {
                    new ExceptionHandler(e, "DriveTrain").print();
                    return 0.0;
                }
            case kMotors.kLeftB:
                try {
                    return leftB.getOutputCurrent();
                } //return the current from motor leftB
                catch (Throwable e) {
                    new ExceptionHandler(e, "DriveTrain").print();
                    return 0.0;
                }
            case kMotors.kRightA:
                try {
                    return rightA.getOutputCurrent();
                } //Return the current from motor rightA
                catch (Throwable e) {
                    new ExceptionHandler(e, "DriveTrain").print();
                    return 0.0;
                }
            case kMotors.kRightB:
                try {
                    return rightB.getOutputCurrent();
                } //Return the current from motor rightB
                catch (Throwable e) {
                    new ExceptionHandler(e, "DriveTrain").print();
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }

    /**
     * Get the average current being drawn from a side of the drivetrain
     * @param side The side to poll. kRight, kLeft
     * @return The current in amps
     */
    public double getSideCurrent(int side) {
        switch (side) {
            case kSides.kLeft:  //Return current from the left side motors
                try {
                    return ((leftA.getOutputCurrent() + leftB.getOutputCurrent()) / 2);
                } catch (Throwable e) {
                    new ExceptionHandler(e, "DriveTrain").print();
                    return 0.0;
                }
            case kSides.kRight: //Return the current from the right side motors
                try {
                    return ((rightA.getOutputCurrent() + rightB.getOutputCurrent()) / 2);
                } catch (Throwable e) {
                    new ExceptionHandler(e, "DriveTrain").print();
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }

    /**
     * Get the average current from the entire drivetrain
     * @return The average current in amps
     */
    public double getAverageCurrent() {
        try {
            //Return the average current for the entire drivetrain
            return ((leftA.getOutputCurrent() + leftB.getOutputCurrent()
                    + rightA.getOutputCurrent() + rightB.getOutputCurrent()) / 4);
        } catch (Throwable e) {
            new ExceptionHandler(e, "DriveTrain").print();
            return 0.0;
        }
    }
    boolean shifted = false, gear = false;

    public void shift(boolean shift) {
        if (shift && !shifted) {
            gear = !gear;
            shiftA.set(gear);
            shiftB.set(!gear);
            shifted = true;
        } else if (!shift) {
            shifted = false;
        }
    }

    /**
     * Motors on DriveTrain
     */
    public interface kMotors {

        public int kLeftA = 0;
        public int kLeftB = 1;
        public int kRightA = 2;
        public int kRightB = 3;
    }

    /**
     * Sides of the drivetrain
     */
    public interface kSides {

        public int kLeft = 4;
        public int kRight = 5;
    }
}
