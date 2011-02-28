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
//import java.lang.Math;

/**
 *  DriveTrain Class. A wrapper class for CANJaguars.
 * @author Jeremy Germita and Gabe Ruiz
 */
public class DriveTrain {
    private CANJaguar leftA;    //Left motor A
    private CANJaguar leftB;    //Left motor B
    private CANJaguar rightA;    //Right motor A
    private CANJaguar rightB;    //Right Motor B

    private Solenoid shiftA = new Solenoid(7);  //Shifter solenoids
    private Solenoid shiftB = new Solenoid(8);

    private Encoder encoder  = new Encoder(10, 11);   //drive Encoders
    //private Encoder rightEnc = new Encoder(6, 7);
    
    private Gyro yaw;   //Gyro

    /**
     * Constructor
     */
    public DriveTrain() {
        encoder.start();
        try {
            leftA  = new CANJaguar(6);    //Left motor A
            leftB  = new CANJaguar(8);    //Left motor B
            rightA = new CANJaguar(2);    //Right motor A
            rightB = new CANJaguar(5);    //Right Motor B
            leftA.configFaultTime(.5);
            leftB.configFaultTime(.5);
            rightA.configFaultTime(.5);
            rightB.configFaultTime(.5);
        } catch(Throwable e){
            new ExceptionHandler(e, "DriveTrain").print();
            System.out.println("ERROR INITIALIZING DRIVETRAIN");
            try {
                leftA.enableControl();
                leftB.enableControl();
                rightA.enableControl();
                rightB.enableControl();
            } catch(Throwable t) {
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
        return encoder.get();
    }

    public void stopEncoder() {
        encoder.stop();
    }

    public void startEncoder() {
        encoder.start();
    }
    
    public void driveStraight(double counts, double throttle, double angle) {
        //final double P = .5
        //double error = angle - yaw.getAngle();
        //double output = (P*angle);
        //if(output > 1) {
        //    output = 1;
        //} else if(-output < -1) {
        //    output = -1;
        //}
        if(getEncoderCounts() < counts) {
            tankDrive(throttle/*-output*/, throttle/*+output*/);
        } else {
            tankDrive(0,0);
        }
    }

    /**
     * Tank Drive Method
     * @param leftV left output
     * @param rightV right output
     */
    public void tankDrive(double leftV, double rightV) {
        try {
            leftA.setX(leftV,(byte) 1);       //Set left motor A
            leftB.setX(leftV,(byte) 1);       //Set left motor B
            rightA.setX(rightV,(byte) 1);     //Set right motor A
            rightB.setX(rightV,(byte) 1);     //Set right motor B
            CANJaguar.updateSyncGroup((byte) 1);
        } catch(Throwable e) {
            new ExceptionHandler(e, "DriveTrain").print();
            try {
                leftA.enableControl();
                leftB.enableControl();
                rightA.enableControl();
                rightB.enableControl();
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }
    
    /**
     * Arcade Drive method
     * @param throttle
     * @param turn
     */
    public void arcadeDrive(double throttle, double turn) {
        try {
            tankDrive(throttle + turn, throttle - turn);
        } catch(Throwable e) {
            new ExceptionHandler(e, "DriveTrain").print();
            try {
                leftA.enableControl();
                leftB.enableControl();
                rightA.enableControl();
                rightB.enableControl();
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * Set the gyro for use with various drivetrain actions
     * @param gyro
     */
    public void setGyro(Gyro gyro) {
        this.yaw = gyro;
    }

    /**
     * Angle drive method. Field centric with skid steer
     * @param gyro The gyro used for control
     * @param throttle The speed to run the motors
     * @param angle The angle to drive to
     */
    public void angleDrive(double throttle, double angle) {
        
        double p = 0, proportional,
                i = 0, d = 0;
        double error, prevError;
        double thresh = 10;

        if(Math.threshold(yaw.getAngle(), (angle+thresh), (angle-thresh))) {
            error = yaw.getAngle();
            proportional = (p*error);
            if(proportional > 1) {
                proportional = 1;
            } else if(proportional < -1){
                proportional = -1;
            }
            arcadeDrive(throttle, (proportional));
        } else {
            arcadeDrive(throttle, 0);
        }
    }

    /**
     * Hold the current position. Will use gyro and encoder to hold the current
     * position.
     * @param hold Whether or not to hold
     */
    public void holdPosition(boolean hold) {
        //TODO: test hold position method
    }
    /**
     * Get Current at a single motor
     * @param motor The motor to get current from. kLeftA, kLeftB, kRightA, kRightB
     * @return The current in amps
     */
    public double getSingleMotorCurrent(int motor) {
        switch(motor) {
            case kMotors.kLeftA:
                try { return leftA.getOutputCurrent(); }    //Return the current from motor leftA
                catch(Throwable e) { new ExceptionHandler(e, "DriveTrain").print(); return 0.0; }
            case kMotors.kLeftB:
                try { return leftB.getOutputCurrent(); }    //return the current from motor leftB
                catch(Throwable e) { new ExceptionHandler(e, "DriveTrain").print(); return 0.0; }
            case kMotors.kRightA:
                try { return rightA.getOutputCurrent(); }   //Return the current from motor rightA
                catch(Throwable e) { new ExceptionHandler(e, "DriveTrain").print(); return 0.0; }
            case kMotors.kRightB:
                try { return rightB.getOutputCurrent(); }   //Return the current from motor rightB
                catch(Throwable e) { new ExceptionHandler(e, "DriveTrain").print(); return 0.0; }
            default: return 0.0;
        }
    }

    /**
     * Get the average current being drawn from a side of the drivetrain
     * @param side The side to poll. kRight, kLeft
     * @return The current in amps
     */
    public double getSideCurrent(int side) {
        switch(side) {
            case kSides.kLeft:  //Return current from the left side motors
                try { return ((leftA.getOutputCurrent() + leftB.getOutputCurrent())/2); }
                catch(Throwable e) { new ExceptionHandler(e, "DriveTrain").print(); return 0.0; }
            case kSides.kRight: //Return the current from the right side motors
                try { return ((rightA.getOutputCurrent() + rightB.getOutputCurrent())/2); }
                catch(Throwable e) { new ExceptionHandler(e, "DriveTrain").print(); return 0.0; }
            default: return 0.0;
        }
    }

    /**
     * Get the average current from the entire drivetrain
     * @return The average current in amps
     */
    public double getAverageCurrent() {
        try {
            //Return the average current for the entire drivetrain
            return ((leftA.getOutputCurrent() + leftB.getOutputCurrent() +
                     rightA.getOutputCurrent() + rightB.getOutputCurrent())/4);
        } catch(Throwable e) {
            new ExceptionHandler(e, "DriveTrain").print();
            return 0.0;
        }
    }
    
    boolean shifted = false, gear = false;
    public void shift(boolean shift) {
        if(shift && !shifted) {
            gear = !gear;
            shiftA.set(gear);
            shiftB.set(!gear);
            shifted = true;
        } else if(!shift) {
            shifted = false;
        }
    }

    /**
     * Motors on DriveTrain
     */
    public interface kMotors {
        public int kLeftA  = 0;
        public int kLeftB  = 1;
        public int kRightA = 2;
        public int kRightB = 3;
    }
    /**
     * Sides of the drivetrain
     */
    public interface kSides {
        public int kLeft  = 4;
        public int kRight = 5;
    }


}
