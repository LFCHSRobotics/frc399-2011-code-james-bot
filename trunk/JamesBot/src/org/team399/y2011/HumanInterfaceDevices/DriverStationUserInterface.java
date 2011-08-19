/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.HumanInterfaceDevices;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;

/**
 * Driver Station User Interface Class. A wrapper class for the Cypress PSoC
 * @author Jeremy Germita and Brad Hall
 */
public class DriverStationUserInterface {

    private DriverStationEnhancedIO m_io;   //IO board object
    private boolean fault = false;

    //Digital channels for the different switches
    final short BLUE_BUTTON        = 11;
    final short RED_BUTTON         = 1;
    final short WHITE_BUTTON       = 9;
    final short BLACK_BUTTON       = 3;
    final short TOGGLE_SWITCH_LEFT = 12;
    final short TOGGLE_SWITCH_RIGHT= 6;
    final short MISSILE_SWITCH     = 2;


    /**
     * Constructor.
     */
    public DriverStationUserInterface() {
        m_io = DriverStation.getInstance().getEnhancedIO(); //Instantiating the object
        //try {
            //m_io.getAnalogIn(1);
        //} catch(EnhancedIOException e) {
            //fault = true;
        //    e.printStackTrace();
        //}
    }



    /**
     * Get the state of the blue button
     * @return blue button's state
     */
    public boolean getBlueButton() {
        return !getDigital(BLUE_BUTTON);
    }

    /**
     * Get the state of the red button
     * @return The red button's state
     */
    public boolean getRedButton() {
        return !getDigital(RED_BUTTON);
    }

    /**
     * Get the state of the white button
     * @return The white button's state
     */
    public boolean getWhiteButton() {
        return !getDigital(WHITE_BUTTON);
    }

    /**
     * Get the state of the black button
     * @return The black button's state
     */
    public boolean getBlackButton() {
        return !getDigital(BLACK_BUTTON);
    }

    /**
     * Get the state of the left toggle switch
     * @return The toggle switch's state
     */
    public boolean getLeftToggleSwitch() {
        return !getDigital(TOGGLE_SWITCH_LEFT);
    }

    /**
     * Get the state of the right toggle switch
     * @return The toggle switch's state
     */
    public boolean getRightToggleSwitch() {
        return getDigital(TOGGLE_SWITCH_RIGHT);
    }

    /**
     * Get the state of the missile switch
     * @return The missle switch's state
     */
    public boolean getMissileSwitch() {
        return !getDigital(MISSILE_SWITCH);
    }

    /**
     * Get the state of a digital input
     * @param which The channel
     * @return The state of the digital input
     */
    public boolean getDigital(int which) {
        //if(!fault) {
            try {
                return m_io.getDigital(which);
            } catch(Exception e) {
                e.printStackTrace();
                //fault = true;
                return false;
            }
        //} else {
        //    return false;
        //}
    }

    /**
     * Get the value of the analog channel specified
     * @param which Which channel
     * @return the value of that channel, usually between 0 and 3.3
     */
    public double getAnalog(int which) {
        //if(!fault) {
            try {
                return m_io.getAnalogIn(which);
            } catch(Exception e) {
                e.printStackTrace();
                //fault = true;
                return 0.0;
            }
        //} else {
        //    return 0.0;
        //}
    }

    /**
     * Return false during normal activity, true if the board is not plugged in
     * @return false during normal activity, true if the board is not plugged in
     */
    public boolean getFault() {
        fault = false;
        try {
            m_io.getAnalogIn(1);
        } catch(EnhancedIOException eioe) {
            eioe.printStackTrace();
            fault = true;
        }
        return fault;
    }

    public void setIndicators(boolean state) {
        
            try {
                m_io.setLED(1, state);
                m_io.setLED(5, state);
            } catch(Exception e) {
                e.printStackTrace();
            }
        
    }
}
