/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.communications;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 * Class containing methods for communicating with th driver station computer
 * @author Jeremy Germita
 */
public class Driverstation {
    private Dashboard m_dashHigh;
    private Dashboard m_dashLow;
    private DriverStationLCD m_lcd;

    //TODO: add to this class.
    //TODO: Dashboard stuff
    //TODO: Driverstation stuff

    /**
     * Constructor.
     */
    public Driverstation() {
        m_dashHigh = DriverStation.getInstance().getDashboardPackerHigh();
        m_dashLow  = DriverStation.getInstance().getDashboardPackerLow();
        m_lcd = DriverStationLCD.getInstance();
    }

    public void writeLCD(int line, String text) {
        //TODO: EDIT THIS METHOD
        switch(line) {
            case 1:
                m_lcd.println(DriverStationLCD.Line.kMain6, line, text);
        }
    }
}
