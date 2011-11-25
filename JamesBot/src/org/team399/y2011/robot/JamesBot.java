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
import org.team399.y2011.HumanInterfaceDevices.SaitekST290Joystick;
import org.team399.y2011.Humans.Driver;
import org.team399.y2011.Humans.Operator;
import org.team399.y2011.robot.actuators.DeploymentMechanism;
import org.team399.y2011.robot.actuators.Flopper;
import org.team399.y2011.communications.FRCDashboard;

/**
 *
 * @author Jeremy Germita, Gabriel Ruiz, Jackie Patton, Brad Hall, Rob Wong
 */
public class JamesBot extends IterativeRobot {

    //Driver inputs/outputs:
    public static Attack3Joystick leftJoy = new Attack3Joystick(1);           //Left Joystick
    public static Attack3Joystick rightJoy = new Attack3Joystick(2);           //Right Joystick
    public static Rumblepad2GamePad gamePad = new Rumblepad2GamePad(4);         //Operator gamepad
    public static SaitekST290Joystick opJoy = new SaitekST290Joystick(3);       //Secondary operator joystick
    public static DriverStationUserInterface io = new DriverStationUserInterface(); //IO Board instance
    public static FRCDashboard db = new FRCDashboard();               //Dashboard instance
    //Actuator outputs:
    public static DriveTrain robot = new DriveTrain();     //DriveTrain instance, contains drive code
    public static DeploymentMechanism deploy = new DeploymentMechanism(3, 4);  //Deployment mechanism instance
    public static Flopper flopper = new Flopper(1, 2);  //Flopper mechanism instance
    public static Arm arm = new Arm(5, 6);  //Arm instance
    public static RollerClaw roller = new RollerClaw();     //Instance of the roller claw
    public static Compressor compressor = new Compressor(14, 1); //Compressor instance
    //Driver/Operator routines
    private Driver driver = new Driver("Jeremy");    //Driver Instance
    private Operator operator = new Operator("Gabe");      //Operator instance
    //Other variables:
    public static int autonomousMode = 1;   //Current selected autonomous mode

    /**
     * Initialize robot
     */
    public void robotInit() {
        System.out.println("Hello World!!");                            //Print out some things to say that it is ready to start
        System.out.println("Started at: " + System.currentTimeMillis());//Print out init time
        arm.print();                                                    //Print arm data at init
    }

    /**
     * Initialize disabled mode
     */
    public void disabledInit() {
        arm.disable();          //Disable arm PID control
        robot.resetGyro();      //reset gyro
        robot.startEncoder();   //start
        robot.resetEncoder();   //and reset encoder
    }
    boolean autonOff = true;   //Disable autonomous switch

    /**
     * Run this during disabled mode
     */
    public void disabledPeriodic() {
        if (autonOff) {                           //If autonomous is disabled
            autonomousMode = 0;                  //Do nothing
        } else {                                 //Else
            if (io.getRightToggleSwitch()) {
                autonomousMode = 2;              //Two tube autonomous
            } else {
                autonomousMode = 1;              //One tube autonomous
            }
        }

        //Don't run autonomous if we don't put a tube in the claw
        if (roller.getLimitSwitch()) {       //if there is a tube in the claw
            System.out.println("Tube In!!");
            autonOff = false;               //run autonomous
        } else {
            System.out.println("No Tube!!");
            autonOff = true;                //Else don't run autonomous
        }

        //Zero arm position if the white button on the driver station is pressed
        if (io.getWhiteButton()) {
            arm.zeroPotentiometer();
        }

        //Turn on IO Board LEDs if autonomous is turned off
        io.setIndicators(autonOff);

        robot.resetGyro();  //Reset gyro

        arm.print();    //Print arm data
    }

    /**
     * Initialize teleoperated mode
     */
    public void teleopInit() {
        compressor.start();                     //Start compressor
        operator.init(Arm.ArmStates.GROUND, 1); //Bring arm to pickup position, initialize operator
        driver.init();                          //Initialize driver
    }

    /**
     * Run this during teleoperated mode
     */
    public void teleopPeriodic() {
        driver.drive();     //Driver stuff
        operator.operate(); //Operater stuff

    }

    public void autonomousInit() {
        //disabledAuton - do nothing in autonomous
        //dumbAuton - dead reckoning drive autonomous. Used in SD, LA
        //competentAuton - gyro assisted heading auton. Used in UT, GAL, IRI, Fall Classic
        //geniusAuton - two tube auton used at Battle at the Border, modified for one tube

        //All autonomous programs require resetting of encoder and gyro, so we will do that
        robot.resetEncoder();           //Reset Encoder
        robot.resetGyro();              //Reset Gyro
        compressor.start();             //Start compressor
        switch (autonomousMode) {
            case 0:
                AutonomousRoutines.disabled();
                break;
            case 1: //Auton 1 requires some initialization, do it.
                AutonomousRoutines.initCompetentAuton();
                break;
            case 2: //Auton 2 requires initialization
                AutonomousRoutines.initGeniusAuton();
                break;
            case 3: //Auton 3 does not exist, therefore we will not initialize it
                break;
            case 4: //Auton 4 does not exist, therefore we will not initialize it
                break;
            default: //By default(IO board broke, buttons broke, etc) do competentAuton stuff
                AutonomousRoutines.initCompetentAuton();
                break; //Init dumbAuton timers
        }
    }

    public void autonomousPeriodic() {

        switch (autonomousMode) {
            case 0:
                AutonomousRoutines.disabled();
                break;
            case 1: //Single Tube Auton
                AutonomousRoutines.competentAuton();
                break;
            case 2: //Double Tube Auton
                AutonomousRoutines.geniusAuton();
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                AutonomousRoutines.competentAuton();
                break;
        }
    }
}
