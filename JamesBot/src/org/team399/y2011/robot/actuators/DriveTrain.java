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

    private Encoder encoder  = new Encoder(8, 9);   //drive Encoder
    private Gyro yaw         = new Gyro(2);         //Gyro

    /**
     * Constructor
     */
    public DriveTrain() {
            encoder.start();
            yaw.reset();
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
        if(angle < 0.0) {
            angle += 360;
        } else if(angle > 360.0) {
            angle -= 360;
        }
        return angle;
    }

    public double getAngle() {
        return yaw.getAngle();
    }

    public void print() {
        System.out.println("Angle: " + getAngle());
    }

    double prevError = 0;
    public void driveStraight(double throttle, double angle) {
        final double P = .15; // the proportional scaler
        final double D = .05;

        double error = angle - yaw.getAngle();
        double outputRight = throttle - ((P*error) - (D*(error - prevError)));
        if(outputRight > 1) {   //this limits our output to the motors to -1/1
            outputRight = 1;
        } else if(-outputRight < -1) {
            outputRight = -1;
        }
        System.out.println("Output: " + outputRight);
        prevError = error;
        tankDrive(-throttle, outputRight);
    }

    public void autoShift() {
        if(getAverageCurrent() > 80) {
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
            tankDrive(-throttle + turn, -(-throttle - turn));
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
/*
    double p = -.0015, proportional, //p=.00015
            i = 0, d = 0, derivative,
            pdOut;
    double error, prevError;
    double thresh = 5;
    /**
     * Angle drive method. Field centric with skid steer
     * @param gyro The gyro used for control
     * @param throttle The speed to run the motors
     * @param angle The angle to drive to
     
    public void angleDrive(double throttle, double angle) {
        
            error = angle - getAngle();
            proportional = (p*error);
            derivative = d*(error-prevError);
            pdOut = proportional;// - derivative;
            if(pdOut > throttle) {
                pdOut = throttle;
            } else if(pdOut < -throttle){
                pdOut = -throttle;
            }
            System.out.println("Angle: " + getAngleWraparound());
            System.out.println("PDOut: " + pdOut);

            tankDrive(throttle + pdOut,throttle + pdOut);
            prevError = error;
    }
*/
    public void lowGear() {
        shiftA.set(false);
        shiftB.set(true);
    }

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
