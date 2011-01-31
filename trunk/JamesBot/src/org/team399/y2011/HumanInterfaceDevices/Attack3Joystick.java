    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.HumanInterfaceDevices;
import edu.wpi.first.wpilibj.Joystick;
/**
 * A wrapper Class for use with the Logitech Attack 3 Joystick
 * @author Jeremy Germita
 */
public class Attack3Joystick {
    private Joystick m_Stick;   //Joystick object
    /**
     * Contructor.
     * @param port The USB port the Joystick is connected to
     */
    public Attack3Joystick(int port) {
        m_Stick = new Joystick(port);
    }

    /**
     * Get the Y axis
     * @return the Y axis
     */
    public double getY() {
        return m_Stick.getRawAxis(2);
    }

    /**
     * Get the X axis
     * @return the X axis
     */
    public double getX() {
        return m_Stick.getRawAxis(1);
    }

    /**
     * Get the throttle
     * @return the throttle
     */
    public double getThrottle() {
        return m_Stick.getRawAxis(3);
    }

    /**
     * Get the state of the trigger
     * @return the trigger state
     */
    public boolean getTrigger() {
        return m_Stick.getRawButton(1);
    }

    /**
     * Get the state the button
     * @param button the button to poll
     * @return the state of the button
     */
    public boolean getButton(int button) {
        return m_Stick.getRawButton(button);
    }
}
