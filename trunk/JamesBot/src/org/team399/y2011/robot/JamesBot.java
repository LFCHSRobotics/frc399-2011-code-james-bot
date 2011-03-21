/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team399.y2011.robot;


import org.team399.y2011.robot.actuators.DriveTrain;
import org.team399.y2011.robot.actuators.Arm;
import org.team399.y2011.robot.actuators.RollerClaw;
import org.team399.y2011.robot.autonomous.AutonomousRoutines;
import org.team399.y2011.HumanInterfaceDevices.Attack3Joystick;
import org.team399.y2011.HumanInterfaceDevices.Rumblepad2GamePad;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Gyro;
import org.team399.y2011.HumanInterfaceDevices.DriverStationUserInterface;
import org.team399.y2011.robot.actuators.DeploymentMechanism;
import org.team399.y2011.robot.actuators.Flopper;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class JamesBot extends IterativeRobot {

    public static DriveTrain robot = new DriveTrain();   //DriveTrain instance, contains drive code
    
    Attack3Joystick leftJoy  = new Attack3Joystick(1);    //Left Joystick
    Attack3Joystick rightJoy = new Attack3Joystick(2);    //Right Joystick
    Rumblepad2GamePad operator = new Rumblepad2GamePad(3);//Operator gamepad

    DriverStationUserInterface io = new DriverStationUserInterface();
    
    DeploymentMechanism deploy = new DeploymentMechanism(3,4);  //Deployment mechanism instance
    Flopper flopper            = new Flopper            (1,2);  //Flopper mechanism instance
    public static Arm arm      = new Arm                (5,6);  //Armkickbutt instance
    public static RollerClaw roller= new RollerClaw();

    public static Gyro yaw = new Gyro(2);
    Compressor compressor = new Compressor(14,1);   //Compressor instance

    int autonomousMode = 0;
    String autonomousSide = "None";
    
    public void disabledInit() {
        arm.disable();  //Disable arm PID control
        
    }

    public void robotInit() {
        yaw.reset();
        robot.stopEncoder();
        robot.startEncoder();
    }

    long startTime;
    

    public void disabledPeriodic() {
       arm.print();    //Print arm pot value

       if(io.getWhiteButton()) {
           autonomousMode = 1;
       } else if(io.getBlackButton()) {
           autonomousMode = 2;
       } else if(io.getRedButton()) {
           autonomousMode = 3;
       } else if(io.getBlueButton()) {
           autonomousMode = 4;
       }
    }

    public void teleopInit() {
        arm.setPoint(Arm.ArmStates.HIGH);
        compressor.start(); //Start compressor
        arm.print();
        arm.reset();
        arm.update();
        startTime = System.currentTimeMillis();
        arm.setSpeedLimit(1);
        robot.tankDrive(0,0);
    }

    public void teleopPeriodic() {
        if(!rightJoy.getButton(3)) {
            robot.tankDrive(leftJoy.getY(),
                            -rightJoy.getY());   //Tank drive, two sticks
        } else {
            robot.arcadeDrive(leftJoy.getX(), rightJoy.getY()); //Arcade Drive
        }
        robot.shift(rightJoy.getTrigger() || leftJoy.getTrigger()); //Shift the drivetrain()

    }

    int currentPoint = 0;
    boolean prevPad = false, currPad = false;;
    public void operate() {
        /**********************************************************************
         * Setpoint cycling code
         */
        currPad = operator.getDPad(Rumblepad2GamePad.DPadStates.UP) ||
                  operator.getDPad(Rumblepad2GamePad.DPadStates.DOWN);  //currPad is true if a button is pressed
        if(operator.getDPad(Rumblepad2GamePad.DPadStates.UP) && currPad != prevPad) {
            currentPoint++;
        } else if(operator.getDPad(Rumblepad2GamePad.DPadStates.DOWN) && currPad != prevPad)  {
            currentPoint--;
        } else {
            if(Math.abs(operator.getRightY()) > 0.1) {
                arm.setPoint(arm.getSetpoint() + (operator.getRightY()*.05));
            }
            if(arm.getPosition() == Arm.ArmStates.GROUND) {    currentPoint = 1;}
            else if(arm.getPosition() == Arm.ArmStates.LOW) { currentPoint = 2;}
            else if(arm.getPosition() == Arm.ArmStates.MID) { currentPoint = 3;}
            else if(arm.getPosition() == Arm.ArmStates.HIGH) { currentPoint = 4;}
        }

        currentPoint = (currentPoint <= 0) ? 0 :
                      ((currentPoint >= 5) ? 5 : currentPoint);


        switch(currentPoint) {
            case 0: arm.setPoint(Arm.ArmStates.INSIDE);        break;
            case 1: arm.setPoint(Arm.ArmStates.GROUND);        break;
            case 2: arm.setPoint(Arm.ArmStates.LOW);           break;
            case 3: arm.setPoint(Arm.ArmStates.MID);           break;
            case 4: arm.setPoint(Arm.ArmStates.HIGH);          break;
            case 5: arm.setPoint(Arm.ArmStates.TOMAHAWK_HIGH); break;
        }
        
        prevPad = currPad;
        /**********************************************************************
         * End Cycling Code
         */

        /**********************************************************************
         * Other operator controls
         */

        //Arm:
        

        //Endgame Stuff:
        flopper.flop(operator.getButton(9) || io.getMissileSwitch());
        deploy.deploy(operator.getButton(1) && operator.getButton(2) ||
                ((io.getBlackButton() || io.getBlueButton() ||
                  io.getRedButton() || io.getWhiteButton())) &&
                  io.getMissileSwitch());

        //Roller Claw Stuff:
        if(operator.getButton(6)) {
            roller.grab(1);                    // Grab tube
        } else if(operator.getButton(8)) {
            roller.grab(-.5);                   // release tube
        } else if(operator.getButton(7)) {
            roller.articulate(.5);              // articulate tube up
        } else if(operator.getButton(5)) {
            roller.articulate(-.5);             // articulate tube down
        } else {
            roller.grab(operator.getLeftY());
        }
    }

    long starttime;
    public void autonomousInit() {
        robot.resetEncoder();
        yaw.reset();
        robot.setGyro(yaw);
        compressor.start(); //Start compressor
        AutonomousRoutines.startTimer();
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
    }

    public void autonomousPeriodic() {
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);

       switch(autonomousMode) {
           case 1:
               AutonomousRoutines.dumbAuton(); break;
           case 2:
               robot.driveStraight(2188*10, .6, 0); break;
           default:
               AutonomousRoutines.dumbAuton(); break;
       }
    }
}