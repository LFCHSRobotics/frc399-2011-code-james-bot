/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team399.y2011.robot;

import com.sun.squawk.util.MathUtils;
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
import org.team399.y2011.communications.FRCDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class JamesBot extends IterativeRobot {

    //Driver inputs/outputs:
    public static Attack3Joystick leftJoy       = new Attack3Joystick(1);           //Left Joystick
    public static Attack3Joystick rightJoy      = new Attack3Joystick(2);           //Right Joystick
    public static Rumblepad2GamePad gamePad     = new Rumblepad2GamePad(3);         //Operator gamepad
    public static DriverStationUserInterface io = new DriverStationUserInterface(); //IO Board instance
    public static FRCDashboard db                  = new FRCDashboard();                  //Dashboard instance

    //Actuator outputs:
    public static DriveTrain robot           = new DriveTrain         ();     //DriveTrain instance, contains drive code
    public static DeploymentMechanism deploy = new DeploymentMechanism(3,4);  //Deployment mechanism instance
    public static Flopper flopper            = new Flopper            (1,2);  //Flopper mechanism instance
    public static Arm arm                    = new Arm                (5,6);  //Arm instance
    public static RollerClaw roller          = new RollerClaw         ();     //Instance of the roller claw
    public static Compressor compressor      = new Compressor         (14,1); //Compressor instance

    //Driver/Operator routines
    private Driver   driver   = new Driver  ("Jeremy");    //Driver Instance
    private Operator operator = new Operator("Gabe");      //Operator instance

    //Other variables:
    public static int autonomousMode = 1;


    /**
     * Initialize robot
     */
    public void robotInit() {
        System.out.println("Hello World!!");
        System.out.println("Started at: " + System.currentTimeMillis());
    }

    /**
     * Initialize disabled mode
     */
    public void disabledInit() {
        arm.disable();  //Disable arm PID control
        robot.resetGyro();
    }
    boolean autonOff = true;   //Disable autonomous switch
    /**
     * Run this during disabled mode
     */
    public void disabledPeriodic() {
        
                                                        //JIC we are paired with a 2-tube scorer
        if(autonOff) {                                  //If autonomous is disabled
            autonomousMode = 0;                         //Do nothing
        } else {                                        //Else
            autonomousMode = 1;
        }

        if(roller.getLimitSwitch()) {
            System.out.println("Tube In!!");
            autonOff = false;
        } else {
            System.out.println("No Tube!!");
            autonOff = true;
        }
        //System.out.println(autonOff);

        io.setIndicators(autonOff);


        /*if(leftJoy.getTrigger() || rightJoy.getTrigger()) { //Manual override JIC the IO board malfunctions
            autonOff = false;
        } else if(!(io.getBlackButton() || io.getRedButton())){
            autonOff = true;
        } else {
            autonOff = false;
        }*/
        //db.disabledPackAll();    //Pack data into dashboard

        arm.print();

        //robot.print();
        //System.out.println("JoyAngle: " + Math.toDegrees(MathUtils.atan2((Math.abs(rightJoy.getX()) > .1 ? rightJoy.getX() : 0),
        //                                                   (Math.abs(rightJoy.getY()) > .1 ? rightJoy.getY() : 0))));
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
        //db.teleopPackAll();
    }

    public void autonomousInit() {
        //All autonomous programs require resetting of encoder and gyro, so we will do that
        robot.resetEncoder();           //Reset Encoder
        robot.resetGyro();              //Reset Gyro
        compressor.start();             //Start compressor
        switch(autonomousMode) {
            case 0: AutonomousRoutines.disabled(); break;
            case 1: //Auton 1 requires some initialization, do it.
                AutonomousRoutines.initCompetentAuton(); break;
            case 2: //Auton 2 requires no initialization. at the moment.
                AutonomousRoutines.initCompetentAuton(); break;
            case 3: //Auton 3 does not exist, therefore we will not initialize it
                break;
            case 4: //Auton 4 does not exist, therefore we will not initialize it
                break;
            default: //By default(IO board broke, buttons broke, etc) do competentAuton stuff
                AutonomousRoutines.initCompetentAuton(); break; //Init dumbAuton timers
       }
    }
    public void autonomousPeriodic() {
         
        switch(autonomousMode) {
            case 0: AutonomousRoutines.disabled(); break;
            case 1: //If you pressed the white button before the match, do the dead reckonining timer autonomous(DRTA)
                //This autonomous uses no sensors and brought us to the finals in SD
                //AutonomousRoutines.dumbAuton(); break;
                AutonomousRoutines.competentAuton(); break;
            case 2: //This autonomous program uses the gyro to go straight
                AutonomousRoutines.competentAuton(); break;
            case 3:
                break;
            case 4:
                break;
            default:
                AutonomousRoutines.competentAuton(); break;

       }
    }
}
