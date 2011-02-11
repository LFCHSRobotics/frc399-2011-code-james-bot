/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team399.y2011.robot;


import org.team399.y2011.robot.actuators.DriveTrain;
import org.team399.y2011.robot.autonomous.LineFollow;
import edu.wpi.first.wpilibj.IterativeRobot;
import org.team399.y2011.HumanInterfaceDevices.Attack3Joystick;
import org.team399.y2011.HumanInterfaceDevices.Rumblepad2GamePad;
import edu.wpi.first.wpilibj.Compressor;
import org.team399.y2011.robot.actuators.Arm;

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

    Arm arm               = new Arm();              //Arm instance
    Compressor compressor = new Compressor(14,1);   //Compressor instance
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

    }


    public void disabledInit() {
        arm.disable();  //Disable arm PID control
    }

    public void teleopInit() {
        compressor.start(); //Start compressor
        arm.enable();   //Enable arm PID
    }

    public void disabledPeriodic() {
        arm.print();    //Print arm pot value
    }

    public void teleopPeriodic() {
        robot.tankDrive(rightJoy.getY(),
                        -leftJoy.getY());   //Tank drive, two sticks
        arm.print();    //Print pot value
        
        arm.setPoint(arm.getSetpoint() +
                -operator.getRightY()*.3); //Arm angle control, operator right stick
        arm.update();   //Update arm pid

        if(operator.getButton(11)) {
            compressor.stop();
        } else if(operator.getButton(5)){
            compressor.start();
        }

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        LineFollow.followLine(0.5);
    }

    
}