/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.autonomous;
import org.team399.y2011.robot.JamesBot;
import org.team399.y2011.robot.actuators.Arm;


/**
 *
 * @author Jeremy Germita and Robert Wong
 */
public class AutonomousRoutines {

    private static long startTime;
    

    /**
     * Initialize dumbAuton components
     */
    public static void initDumbAuton() {
        startTime = System.currentTimeMillis();
        JamesBot.arm.setElbow(false);    //Hold arm in
        JamesBot.arm.enable();          //Enable closed loop control on arm
        JamesBot.arm.setSpeedLimit(.7); //Halve the speed limit on the arm
    }

    /**
     * Actual dumbAuton code
     * This autonomous program brought us to the finals in the San Diego Regional
     */
    public static void dumbAuton() {
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);
        
        JamesBot.arm.update();
        if(System.currentTimeMillis() - startTime > 3100 && System.currentTimeMillis() - startTime < 9750) {            
            JamesBot.robot.arcadeDrive(.5, 0);
        }
        if(System.currentTimeMillis() - startTime > 9750 && !(System.currentTimeMillis() - startTime > 11000)) {
            JamesBot.robot.arcadeDrive(0, 0);
            JamesBot.roller.grab(-.5);
        }

        if(System.currentTimeMillis() - startTime > 11000) {
            JamesBot.robot.arcadeDrive(-.5, 0);
        }

    }


    /**
     * Initialize all compeonents for competent auton
     */
    public static void initCompetentAuton() {
        startTime = System.currentTimeMillis();     //Start the timer
        JamesBot.arm.setElbow(false);               //unfold arm
        JamesBot.arm.enable();                      //Enable closed loop control on arm
        JamesBot.arm.setSpeedLimit(.8);             //Limit the speed on the arm
        JamesBot.arm.setPoint(Arm.ArmStates.HIGH);  //Bring the arm to scoring position
        JamesBot.robot.resetGyro();                 //Reset the gyroscope
    }


    static boolean driveForward;
    static boolean releaseTube;
    static boolean pullBack;
    private static int percent = 0;

    /**
     * Competent Auton code
     * This autonomous program Helped us to win the Utah regional
     */
    public static void competentAuton() {
        JamesBot.robot.lowGear();

        JamesBot.db.packAll(true, 2, percent);

        driveForward = System.currentTimeMillis() - startTime > 0 && System.currentTimeMillis() - startTime < 6300;
        releaseTube  = System.currentTimeMillis() - startTime > 6300 && System.currentTimeMillis() - startTime < 6500;
        pullBack     = System.currentTimeMillis() - startTime > 6500 && System.currentTimeMillis() - startTime < 8500;
        JamesBot.arm.update();
        if(driveForward) {
            JamesBot.robot.driveStraight(.5, 0);
            percent = 25;
        }
        if(releaseTube) {
            JamesBot.robot.arcadeDrive(0, 0);
            JamesBot.roller.grab(-.5);
            percent = 50;
        }

        if(pullBack) {
            JamesBot.robot.driveStraight(-.7, 0);
            percent = 75;
        }

        if(System.currentTimeMillis() - startTime > 9500 && System.currentTimeMillis() - startTime < 10000) {
            
            JamesBot.arm.setPoint(Arm.ArmStates.INSIDE);
            JamesBot.roller.grab(0);
            JamesBot.arm.setElbow(true);
            JamesBot.robot.arcadeDrive(0,0);
            percent = 100;
        }

        /**
         * TTTTT EEEEE SSSSS TTTTT  !!
         *   T   E     S       T    !!
         *   T   EEE   SSSSS   T    !!
         *   T   E         S   T
             T   EEEEE SSSSS   T    !!
         */
        if(System.currentTimeMillis() - startTime > 11000) {
            if(JamesBot.robot.getAngle() < 180) {
                JamesBot.robot.arcadeDrive(0, - .5);
            } else {
                JamesBot.robot.arcadeDrive(0,0);
            }
        }
        JamesBot.db.commit();
    }

    /**
     * Do nothing in autonomous mode.
     * It works. Proven in the shop.
     * One flaw: does not score
     */
    public static void disabled() {

        System.out.println("Sad robot is sad during autonomous :(");
        for(int i = 0; i < 100; i++) {
            JamesBot.db.packAll(false, 0, i);
            JamesBot.db.commit();

        }
    }

}