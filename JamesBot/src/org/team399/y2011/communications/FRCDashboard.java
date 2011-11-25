/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team399.y2011.communications;

import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStation;
import org.team399.y2011.robot.JamesBot;
import org.team399.y2011.robot.actuators.Arm;

/**
 * Class containing methods for communicating with th driver station computer
 * @author Jeremy Germita
 */
public class FRCDashboard {
    //private FRCDashboard m_dashHigh; //Commented out because it is never used. One less object to initialize

    private Dashboard m_dashLow;
    private DriverStationLCD m_lcd;
    private DriverStation m_ds;
    private String m_diag_print = null;

    /**
     * Constructor.
     */
    public FRCDashboard() {
        //m_dashHigh = DriverStation.getInstance().getDashboardPackerHigh();
        m_dashLow = DriverStation.getInstance().getDashboardPackerLow();
        m_lcd = DriverStationLCD.getInstance();
        m_ds = DriverStation.getInstance();
    }

    public void writeLCD(int line, String text) {
        //TODO: EDIT THIS METHOD
        switch (line) {
            case 1:
                m_lcd.println(DriverStationLCD.Line.kMain6, line, text);
                break;
        }
    }

    public void sendDouble(double data) {
        m_dashLow.addDouble(data);
    }

    public void sendBoolean(boolean data) {
        m_dashLow.addBoolean(data);
    }

    public void sendInt(int data) {
        m_dashLow.addInt(data);
    }

    public void sendString(String data) {
        m_dashLow.addString(data);
    }

    public void setDiagnosticPrintout(String data) {
        m_diag_print = data;
    }

    public void commit() {
        m_dashLow.commit();
    }

    public String getStation() {
        String outDataAlliance = m_ds.getAlliance() == DriverStation.Alliance.kBlue
                ? "Blue " : "Red ";
        return outDataAlliance + m_ds.getLocation();
    }
    private boolean left = false, right = false, gamepad = false;

    public void disabledPackAll() {
        if (JamesBot.leftJoy.getAny()) {
            left = true;
        }

        if (JamesBot.rightJoy.getAny()) {
            right = true;
        }

        if (JamesBot.gamePad.getAny()) {
            gamepad = true;
        }
        sendBoolean(left);
        sendBoolean(right);
        sendBoolean(gamepad);
        sendBoolean(!JamesBot.io.getFault());
        sendBoolean(JamesBot.autonomousMode == 1);
        sendDouble(Arm.ArmStates.LOWER_LIMIT - JamesBot.arm.getPosition());
        sendDouble(Arm.ArmStates.LOWER_LIMIT - JamesBot.arm.getSetpoint());
        sendDouble(JamesBot.arm.getCurrent());
        sendString(m_diag_print);
        commit();
    }

    public void autonomousPackAll() {
        sendBoolean(true);
        sendBoolean(true);
        sendBoolean(true);
        sendBoolean(true);
        sendBoolean(JamesBot.autonomousMode == 1);
        sendDouble(Arm.ArmStates.LOWER_LIMIT - JamesBot.arm.getPosition());
        sendDouble(Arm.ArmStates.LOWER_LIMIT - JamesBot.arm.getSetpoint());
        sendDouble(JamesBot.arm.getCurrent());
        sendString(m_diag_print);
        commit();
    }

    public void teleopPackAll() {
        sendBoolean(true);
        sendBoolean(true);
        sendBoolean(true);
        sendBoolean(true);
        sendBoolean(true);
        sendDouble(Arm.ArmStates.LOWER_LIMIT - JamesBot.arm.getPosition());
        sendDouble(Arm.ArmStates.LOWER_LIMIT - JamesBot.arm.getSetpoint());
        sendDouble(JamesBot.arm.getCurrent());
        sendString(m_diag_print);
        commit();
    }
}
