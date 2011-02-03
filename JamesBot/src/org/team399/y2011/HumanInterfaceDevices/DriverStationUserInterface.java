/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.HumanInterfaceDevices;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Driver Station User Interface Class. A wrapper class for the Cypress PSoC
 * @author Jeremy Germita and Brad Hall
 */
public class DriverStationUserInterface {

    private DriverStationEnhancedIO m_io;   //IO board object

    final short BLUE_BUTTON   = 1;
    final short RED_BUTTON    = 2;
    final short WHITE_BUTTON  = 3;
    final short BLACK_BUTTON  = 4;
    final short TOGGLE_SWITCH = 5;
    final short MISSLE_SWITCH = 6;
    final short EASY_BUTTON   = 7;


    public DriverStationUserInterface() {
        m_io = DriverStation.getInstance().getEnhancedIO(); //Instantiating the object
    }

    public boolean getBlueSwitch() {
        try {
            return m_io.getDigital(BLUE_BUTTON);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
