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

    //public static LineSensorArray lsa = new LineSensorArray(1,2,3);

    
    DeploymentMechanism deploy = new DeploymentMechanism(3,4);  //Deployment mechanism instance
    //PincherClaw claw           = new PincherClaw        (3,4);  //Pincher claw instance
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

    public void disabledPeriodic() {
       arm.print();    //Print arm pot value

       //arm.setPoint(arm.getPosition());
       //System.out.println("Enc: " + robot.getEncoderCounts());
       //System.out.println("Gyro:" + yaw.getAngle());

       if(io.getWhiteButton()) {
           autonomousMode = 1;
       } else if(io.getBlackButton()) {
           autonomousMode = 2;
       } else if(io.getRedButton()) {
           autonomousMode = 3;
       } else if(io.getBlueButton()) {
           autonomousMode = 4;
       }
       //System.out.println("Autonomous Mode: " + autonomousMode);
    }

    public void teleopPeriodic() {
        if(!rightJoy.getButton(3)) {
            robot.tankDrive(leftJoy.getY(),
                            -rightJoy.getY());   //Tank drive, two sticks
        } else {
            robot.arcadeDrive(leftJoy.getX(), rightJoy.getY()); //Arcade Drive
        }
        robot.shift(rightJoy.getTrigger() || leftJoy.getTrigger()); //Shift the drivetrain()

        //claw.grab(operator.getButton(6));
        
        arm.enable();/*
        if(operator.getButton(2)) {             //If the operator presses button 2,
            arm.setPoint(Arm.ArmStates.LOW);    //Bring the arm into the low position
        } else if(operator.getButton(3)) {      //Else if they press button 3,
            arm.setPoint(Arm.ArmStates.MID);    //Bring the arm into the mid position
        } else if(operator.getButton(4)) {      //else if they press button 4,
            arm.setPoint(Arm.ArmStates.HIGH);   //Bring the arm into the high posision
        } else if(operator.getButton(1)) {      //Else if they press button 1,
            arm.setPoint(Arm.ArmStates.GROUND); //Bring it into the ground position
        }*/
        
        if(Math.abs(operator.getRightY()) > 0.1) {
            arm.setPoint(arm.getSetpoint() + (operator.getRightY()*.05));
        }
        //arm.set(-operator.getRightY()*.5);
        

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
            //roller.articulate(operator.getLeftX());
        }




        arm.fold(operator.getButton(10));
        flopper.flop(operator.getButton(9));
        deploy.deploy(operator.getButton(1) && operator.getButton(2) );
        arm.update();   //Update arm pid
    }
    long starttime;
    public void autonomousInit() {
        /*
        //arm.fold(true); //commented out by john and brian to prevent folding in frame
        compressor.start();
        arm.fold(false);
        arm.setSpeedLimit(.3);
        arm.enable();
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
        //AutonomousRoutines.autonOne();
        AutonomousRoutines.setSide(autonomousSide);
        starttime = System.currentTimeMillis();*/
        robot.resetEncoder();
        yaw.reset();
        robot.setGyro(yaw);
        compressor.start(); //Start compressor
        AutonomousRoutines.startTimer();
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
    }

    public void autonomousPeriodic() {
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
       //System.out.println("Enc: " + robot.getEncoderCounts());
       //System.out.println("Gyro:" + yaw.getAngle());
       //AutonomousRoutines.oneTubeAuton();
       //AutonomousRoutines.hold();

       switch(autonomousMode) {
           case 1:
               AutonomousRoutines.dumbAuton(); break;
           case 2:
               robot.driveStraight(2188*10, .6, 0); break;
           default:
               AutonomousRoutines.dumbAuton(); break;
       }
       //AutonomousRoutines.dumbAuton();
       //robot.tankDrive(-.5, .5);
    }
}