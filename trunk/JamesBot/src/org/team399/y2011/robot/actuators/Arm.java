/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;

import edu.wpi.first.wpilibj.CANJaguar;

/**
 * Arm class
 * @author Jeremy Germita
 */
public class Arm {

    //TODO:
    //IMPLEMENT JUSTIN'S CHANGES.
    //
    private CANJaguar armA; //Instance of arm CAN Jaguar, A
    private CANJaguar armB; //Instance of arm CAN Jaguar, B
    
    public Arm() {
        try {
            armA = new CANJaguar(4);    //armA is a CANJaguar with the address 6
            armB = new CANJaguar(7);    //armB is a CANJaguar with the address 7
            armA.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            armA.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            //Set the position reference for this jaguar to a pot
            armA.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.print("ERROR INITIALIZING ARM");
        }
    }

    public void set(double value) {
        try {
            armA.setX(value, (byte) 2);   //Set armA to the argument, value
            armB.setX(-value, (byte) 2);  //Set armB to the argument, value, times -1
            CANJaguar.updateSyncGroup((byte) 2);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private double processValue;    //The potentiometer input
    private final double P = 0, I = 0, D = 0; //P, I, and D values
    private double error, prevError, output, integral, derivative; //Other values needed by PID

    public void setpoint(double point) {
        try {
            processValue = armA.getPosition();      //processValue is assigned the value of the pot
            error        = point - processValue;    //error is the distance between the setpoint and process value
            integral     = prevError + error;       //integral is the sum of the current and previous errors
            derivative   = error-prevError;         //derivative is the rate of change between the current and prevErrors
            output       = (P*error) +              //Calculate PID output
                           (I*integral) +
                           (D*derivative);
            prevError    = error;                   //prevError is now equal to error
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
