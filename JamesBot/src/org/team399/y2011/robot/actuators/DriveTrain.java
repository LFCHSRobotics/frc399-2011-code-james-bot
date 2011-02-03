/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Gyro;
import org.team399.y2011.robot.utilities.Math;
//import java.lang.Math;

/**
 *  DriveTrain Class. A wrapper class for CANJaguars.
 * @author Jeremy Germita
 */
public class DriveTrain {
    private CANJaguar leftA;    //Left motor A
    private CANJaguar leftB;    //Left motor B
    private CANJaguar rightA;    //Right motor A
    private CANJaguar rightB;    //Right Motor B
    
    private Gyro yaw;

    /**
     * Constructor
     */
    public DriveTrain() {
        try {
            leftA  = new CANJaguar(2);    //Left motor A
            leftB  = new CANJaguar(3);    //Left motor B
            rightA = new CANJaguar(4);    //Right motor A
            rightB = new CANJaguar(5);    //Right Motor B
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR INITIALIZING DRIVETRAIN");
        }
    }

    /**
     * Tank Drive Method
     * @param leftV left output
     * @param rightV right output
     */
    public void tankDrive(double leftV, double rightV) {
        try {
            leftA  = new CANJaguar(2);    //Left motor A
            leftB  = new CANJaguar(3);    //Left motor B
            rightA = new CANJaguar(4);    //Right motor A
            rightB = new CANJaguar(5);    //Right Motor B
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            leftA.setX(leftV);       //Set left motor A
            leftB.setX(leftV);       //Set left motor B
            rightA.setX(rightV);     //Set right motor A
            rightB.setX(rightV);     //Set right motor B
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Arcade Drive method
     * @param throttle
     * @param turn
     */
    public void arcadeDrive(double throttle, double turn) {
        try {
            leftA  = new CANJaguar(2);    //Left motor A
            leftB  = new CANJaguar(3);    //Left motor B
            rightA = new CANJaguar(4);    //Right motor A
            rightB = new CANJaguar(5);    //Right Motor B
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            leftA.setX(throttle + turn);     //Set left motor A
            leftB.setX(throttle + turn);     //Set left motor B
            rightA.setX(throttle - turn);    //Set right motor A
            rightB.setX(throttle - turn);    //Set right motor B
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Angle drive method. Field centric with skid steer
     * @param gyro The gyro used for control
     * @param throttle The speed to run the motors
     * @param angle The angle to drive to
     */
    public void angleDrive(Gyro gyro, double throttle, double angle) {
        this.yaw = gyro;
        double p, i, d;
        double error, prevError;
        double thresh = 10;

        if(Math.threshold(yaw.getAngle(), (angle+thresh), (angle-thresh))) {

        }
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
                catch(Exception e) { e.printStackTrace(); return 0.0; }
            case kMotors.kLeftB:
                try { return leftB.getOutputCurrent(); }    //return the current from motor leftB
                catch(Exception e) { e.printStackTrace(); return 0.0; }
            case kMotors.kRightA:
                try { return rightA.getOutputCurrent(); }   //Return the current from motor rightA
                catch(Exception e) { e.printStackTrace(); return 0.0; }
            case kMotors.kRightB:
                try { return rightB.getOutputCurrent(); }   //Return the current from motor rightB
                catch(Exception e) { e.printStackTrace(); return 0.0; }
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
                catch(Exception e) { e.printStackTrace(); return 0.0; }
            case kSides.kRight: //Return the current from the right side motors
                try { return ((rightA.getOutputCurrent() + rightB.getOutputCurrent())/2); }
                catch(Exception e) { e.printStackTrace(); return 0.0; }
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
        } catch(Exception e) {
            e.printStackTrace();
            return 0.0;
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
