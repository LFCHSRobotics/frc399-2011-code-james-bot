/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.communications;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Class containing methods for communicating with th driver station computer
 * @author Jeremy Germita
 */
public class Dashboard {
    //private Dashboard m_dashHigh; //Commented out because it is never used. One less object to initialize
    private Dashboard m_dashLow;
    private DriverStationLCD m_lcd;
    private DriverStation m_ds;
    //TODO: add to this class.
    //TODO: Dashboard stuff
    //TODO: Dashboard stuff
    //TODO:

    /**
     * Constructor.
     */
    public Dashboard() {
        //m_dashHigh = DriverStation.getInstance().getDashboardPackerHigh();
        m_dashLow  = DriverStation.getInstance().getDashboardPackerLow();
        m_lcd = DriverStationLCD.getInstance();
        m_ds = DriverStation.getInstance();
    }

    public void writeLCD(int line, String text) {
        //TODO: EDIT THIS METHOD
        switch(line) {
            case 1: m_lcd.println(DriverStationLCD.Line.kMain6, line, text); break;
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

    public void packAll(boolean boolData, int intDataA, int intDataB) {
        sendBoolean(boolData);
        sendInt(intDataA);
        sendInt(intDataB);
    }

    public void commit() {
        m_dashLow.commit();
    }
    
    public String getStation() {
        String outDataAlliance = m_ds.getAlliance() == DriverStation.Alliance.kBlue ?
                          "Blue " : "Red ";
        return outDataAlliance + m_ds.getLocation();
    }

}