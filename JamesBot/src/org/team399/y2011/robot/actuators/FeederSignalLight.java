/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.actuators;
import edu.wpi.first.wpilibj.Relay;

/**
 *  Feeder Signal light Class
 * @author Jeremy Germita
 */
public class FeederSignalLight {
    private Relay red, white, blue;
    
    /**
     * Constructor
     * @param redPort Relay port for Red light
     * @param whitePort Relay port for White Light
     * @param bluePort Relay port for Blue Light
     */
    public FeederSignalLight(int redPort, int whitePort, int bluePort) {
        red = new Relay(redPort);
        white = new Relay(whitePort);
        blue = new Relay(bluePort);
        
    }
    
    /**
     * Set the red light
     * @param state True = on, False = false
     */
    public void setRed(boolean state) {
        Relay.Value setV = ((state) ? Relay.Value.kForward : Relay.Value.kForward);
        red.set(setV);
    }
    
    /**
     * Set the blue light
     * @param state True = on, False = false
     */
    public void setWhite(boolean state) {
        Relay.Value setV = ((state) ? Relay.Value.kForward : Relay.Value.kForward);
        white.set(setV);
    }
    
    /**
     * Set the blue light
     * @param state True = on, False = false
     */
    public void setBlue(boolean state) {
        Relay.Value setV = ((state) ? Relay.Value.kForward : Relay.Value.kForward);
        blue.set(setV);
    }
    
    /**
     * Set ALL of the lights in one line
     * 0x00 All off
     * 0x01 Red on, rest off
     * 0x02 White on, rest off
     * 0x03 Blue on, rest off
     * 0x04 Red + White on, Blue off
     * 0x05 Red + Blue on, White off
     * 0x06 White + Blue on, Red, off
     * 0x07 All on
     * @param allState The value listed above.
     */
    public void setAll(int allState) {
        switch(allState) {
            case 0x00:
                setRed(false);
                setWhite(false);
                setBlue(false);
                break;
            case 0x01:
                setRed(true);
                setWhite(false);
                setBlue(false);
                break;
            case 0x02:
                setRed(false);
                setWhite(true);
                setBlue(false);
                break;
            case 0x03:
                setRed(false);
                setWhite(false);
                setBlue(true);
                break;
            case 0x04:
                setRed(true);
                setWhite(true);
                setBlue(false);
                break;
            case 0x05:
                setRed(true);
                setWhite(false);
                setBlue(true);
                break;
            case 0x06:
                setRed(false);
                setWhite(true);
                setBlue(true);
                break;
            case 0x07:
                setRed(true);
                setWhite(true);
                setBlue(true);
                break;
            default:
                setRed(false);
                setWhite(false);
                setBlue(false);
                break;
        }
    }
}
