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
import org.team399.y2011.HumanInterfaceDevices.DriverStationUserInterface;
import org.team399.y2011.Humans.Driver;
import org.team399.y2011.Humans.Operator;
import org.team399.y2011.robot.actuators.DeploymentMechanism;
import org.team399.y2011.robot.actuators.Flopper;
import org.team399.y2011.communications.Driverstation;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class JamesBot extends IterativeRobot {

    public static Attack3Joystick leftJoy       = new Attack3Joystick(1);           //Left Joystick
    public static Attack3Joystick rightJoy      = new Attack3Joystick(2);           //Right Joystick
    public static Rumblepad2GamePad gamePad     = new Rumblepad2GamePad(3);         //Operator gamepad
    public static DriverStationUserInterface io = new DriverStationUserInterface(); //IO Board instance
    public static Driverstation db              = new Driverstation();

    public static DriveTrain robot           = new DriveTrain         ();     //DriveTrain instance, contains drive code
    public static DeploymentMechanism deploy = new DeploymentMechanism(3,4);  //Deployment mechanism instance
    public static Flopper flopper            = new Flopper            (1,2);  //Flopper mechanism instance
    public static Arm arm                    = new Arm                (5,6);  //Arm instance
    public static RollerClaw roller          = new RollerClaw         ();     //Instance of the roller claw
    public static Compressor compressor      = new Compressor         (14,1); //Compressor instance

    private Driver   driver   = new Driver  ("Sam");    //Driver Instance
    private Operator operator = new Operator("Jeremy"); //Operator instance

    public static int autonomousMode = 0;
    
    public void disabledInit() {
        arm.disable();  //Disable arm PID control    
    }

    public void robotInit() {

    }

    long startTime;

    public void disabledPeriodic() {
       //arm.print();    //Print arm pot value
       //db.sendDouble(arm.getPosition());
       //db.sendDouble(arm.getSetpoint());
       //db.commit();

       /*if(io.getRightToggleSwitch()) {
           autonomousMode = 0;
       } else*/
       
       
       
       //robot.driveStraight(0, 0);
       
       /*if(io.getWhiteButton()) {
           autonomousMode = 1;
       } else if(io.getBlackButton()) {
           autonomousMode = 2;
       } else if(io.getRedButton()) {
           autonomousMode = 3;
       } else if(io.getBlueButton()) {
           autonomousMode = 4;
       }*/

       autonomousMode = 2;
    }

    public void teleopInit() {
        
        compressor.start(); //Start compressor
        startTime = System.currentTimeMillis();
        operator.init(Arm.ArmStates.GROUND, 1);
        driver.init();
    }

    public void teleopPeriodic() {
        driver.drive();     //Driver stuff
        arm.print();    //Print arm pot value
        operator.operate(); //Operater stuff
    }


    long starttime;
    public void autonomousInit() {
        //All autonomous programs require resetting of encoder and gyro, so we will do that
        robot.resetEncoder();           //Reset Encoder
        robot.resetGyro();              //Reset Gyro
        compressor.start();             //Start compressor
        switch(autonomousMode) {
            case 1: //Auton 1 requires some initialization, do it.
                AutonomousRoutines.initDumbAuton(); break; //Init dumbAuton timers
            case 2: //Auton 2 requires no initialization. at the moment.
                AutonomousRoutines.initCompetentAuton();
                break;
            case 3: //Auton 3 does not exist, therefore we will not initialize it
                break;
            case 4: //Auton 4 does not exits, therefore we will not initialize it
                break;
            default: //By default(IO board broke, buttons broke, etc) do dumbAuton stuff
                AutonomousRoutines.initDumbAuton(); break; //Init dumbAuton timers
       }
    }
    public void autonomousPeriodic() {
        switch(autonomousMode) {
            case 1: //If you pressed the white button before the match, do the dead reckonining timer autonomous(DRTA)
                //This autonomous uses no sensors and brought us to the finals in SD
                AutonomousRoutines.dumbAuton(); break;
            case 2: //If you pressed the black one, do the autonomous that does nothing but drive straight
                //Note: This is not a real autonomous program. Just a test for gyros and encoders
                AutonomousRoutines.competentAuton();
            case 3:
                break;
            case 4:
                break;
            default: //Otherwise, do the DRTA
                AutonomousRoutines.dumbAuton(); break;
                //robot.driveStraight(.5, 0); break;
       }
    }
}