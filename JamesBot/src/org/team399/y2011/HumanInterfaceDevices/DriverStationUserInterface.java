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

    final short BLUE_BUTTON        = 11;
    final short RED_BUTTON         = 1;
    final short WHITE_BUTTON       = 9;
    final short BLACK_BUTTON       = 3;
    final short TOGGLE_SWITCH_LEFT = 12;
    final short TOGGLE_SWITCH_RIGHT= 6;
    final short MISSILE_SWITCH     = 2;
    final short EASY_BUTTON        = 7;


    /**
     * Constructor.
     */

    public DriverStationUserInterface() {
        m_io = DriverStation.getInstance().getEnhancedIO(); //Instantiating the object
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
     * Get the state of the easy button
     * @return <b><i> that was easy </i></b>(The state of the easy button)
     */
    public boolean getEasyButton() {
        return getDigital(EASY_BUTTON);
    }

    /**
     * Get the state of a digital input
     * @param which The channel
     * @return The state of the digital input
     */
    public boolean getDigital(int which) {
        try {
            return m_io.getDigital(which);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getAnalog(int which) {
        try {
            return m_io.getAnalogIn(which);
        } catch(Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
