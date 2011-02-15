/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team399.y2011.robot;


import org.team399.y2011.robot.actuators.DriveTrain;
import org.team399.y2011.robot.actuators.Arm;
import org.team399.y2011.robot.actuators.PincherClaw;
import org.team399.y2011.robot.autonomous.LineFollow;
import org.team399.y2011.robot.autonomous.AutonomousRoutines;
import org.team399.y2011.HumanInterfaceDevices.Attack3Joystick;
import org.team399.y2011.HumanInterfaceDevices.Rumblepad2GamePad;
import org.team399.y2011.HumanInterfaceDevices.DriverStationUserInterface;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;


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
    //DriverStationUserInterface io = new DriverStationUserInterface();

    Arm arm          = new Arm();              //Arm instance
    PincherClaw claw = new PincherClaw(3,4);

    Compressor compressor = new Compressor(14,1);   //Compressor instance

    int autonomousMode = 0;
    String autonomousSide = "None";
    // boolean clawOpen = false;                     // Toggle variable for claw JKG 2/14
    
    public void disabledInit() {
        arm.disable();  //Disable arm PID control
    }

    public void robotInit() {

    }

    public void teleopInit() {
        compressor.start(); //Start compressor
        arm.enable();       //Enable arm PID
        arm.setPoint(Arm.ArmStates.GROUND); //Bring the arm down to the ground
        // claw.grab(true);                // initialize the claw closed JKG 2/14
    }

    public void disabledPeriodic() {
        arm.print();    //Print arm pot value
       
    }

    public void teleopPeriodic() {
        robot.tankDrive(leftJoy.getY(),
                        -rightJoy.getY());   //Tank drive, two sticks
        robot.shift(rightJoy.getTrigger() || leftJoy.getTrigger());

        claw.grab(operator.getButton(6));
        //arm.setPoint(arm.getSetpoint() + (-(operator.getRightY())*.05));
        if(!(operator.getRightY() > .05 || operator.getRightY() < -.05)) {
            arm.enable();
            if(operator.getButton(2)) {             //If the operator presses button 2,
                arm.setPoint(Arm.ArmStates.LOW);    //Bring the arm into the low position
            } else if(operator.getButton(3)) {      //Else if they press button 3,
                arm.setPoint(Arm.ArmStates.MID);    //Bring the arm into the mid position
            } else if(operator.getButton(4)) {      //else if they press button 4,
                arm.setPoint(Arm.ArmStates.HIGH);   //Bring the arm into the high posision
            } else if(operator.getButton(1)) {      //Else if they press button 1,
                arm.setPoint(Arm.ArmStates.GROUND); //Bring it into the ground position
            } else if(operator.getButton(10)) {      //Else if they press button 10,
                arm.setPoint(Arm.ArmStates.INSIDE); //Stow the arm
            }
        } else {
            arm.disable();
            arm.set((-operator.getRightY())*.4);
            arm.setPoint(arm.getPosition());
        }
        
        arm.fold(operator.getButton(5));

        arm.update();   //Update arm pid

    }

    public void teleopContinuous() {
    }

    public void autonomousInit() {
        arm.enable();
        AutonomousRoutines.setSide(autonomousSide);

        // claw.grab(true);                 // JKG 2/14 initializes the claw closed
    }
    public void autonomousContinuous() {
        switch(autonomousMode) {
            case 0: AutonomousRoutines.disabled(); break;
            case 1: AutonomousRoutines.autonOne(); break;
            case 2: AutonomousRoutines.autonTwo(); break;
        }


    }

    
    }