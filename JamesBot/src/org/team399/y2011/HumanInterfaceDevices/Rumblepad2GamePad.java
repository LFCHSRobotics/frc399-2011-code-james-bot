/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.HumanInterfaceDevices;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A joystick wrapper class for the Rumblepad 2 Gamepad We will be using
 * @author Jeremy Germita
 */
public class Rumblepad2GamePad {
    private Joystick m_Pad;   //Joystick object
    /**
     * Contructor.
     * @param port The USB port the gamepad is connected to
     */
    public Rumblepad2GamePad(int port) {
        m_Pad = new Joystick(port);
    }

    /**
     * Get the left Y axis
     * @return the Y axis
     */
    public double getLeftY() {
        return m_Pad.getRawAxis(2);
    }

    /**
     * Get the left X axis
     * @return the X axis
     */
    public double getLeftX() {
        return m_Pad.getRawAxis(1);
    }
    /**
     * Get the left Y axis
     * @return the Y axis
     */
    public double getRightY() {
        return m_Pad.getRawAxis(2);
    }

    /**
     * Get the right X axis
     * @return the X axis
     */
    public double getRightX() {
        return m_Pad.getRawAxis(1);
    }

    /**
     * Get the state the button
     * @param button the button to poll
     * @return the state of the button
     */
    public boolean getButton(int button) {
        return m_Pad.getRawButton(button);
    }
}
