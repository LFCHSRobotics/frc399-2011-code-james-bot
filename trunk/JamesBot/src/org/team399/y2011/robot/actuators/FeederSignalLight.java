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
        red   = new Relay(redPort);     //Instantiate the red relay on redPort
        white = new Relay(whitePort);   //Instantiate the white relay on whitePort
        blue  = new Relay(bluePort);    //Instantiate the blue relay on bluePort
    }
    
    /**
     * Set the red light
     * @param state True = on, False = false
     */
    public void setRed(boolean state) {
        //The relay value to set
        Relay.Value setV = ((state) ? Relay.Value.kForward : Relay.Value.kForward);
        red.set(setV);  //Set the relay to the value
    }
    
    /**
     * Set the white light
     * @param state True = on, False = false
     */
    public void setWhite(boolean state) {
        //Relay value to be set
        Relay.Value setV = ((state) ? Relay.Value.kForward : Relay.Value.kForward);
        white.set(setV);    //Set the relay to the value
    }
    
    /**
     * Set the blue light
     * @param state True = on, False = false
     */
    public void setBlue(boolean state) {
        //The relay value to be set
        Relay.Value setV = ((state) ? Relay.Value.kForward : Relay.Value.kForward);
        blue.set(setV); //Set the relay to the value
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
                setRed(false);      //Red off
                setWhite(false);    //White off
                setBlue(false);     //Blue off
                break;
            case 0x01:
                setRed(true);       //Red on
                setWhite(false);    //White off
                setBlue(false);     //Blue off
                break;
            case 0x02:
                setRed(false);      //Red off
                setWhite(true);     //White on
                setBlue(false);     //Blue off
                break;
            case 0x03:
                setRed(false);      //Red off
                setWhite(false);    //White off
                setBlue(true);      //Blue off
                break;
            case 0x04:
                setRed(true);       //Red on
                setWhite(true);     //White on
                setBlue(false);     //Blue off
                break;
            case 0x05:
                setRed(true);       //Red on
                setWhite(false);    //White off
                setBlue(true);      //Blue on
                break;
            case 0x06:
                setRed(false);      //Red off
                setWhite(true);     //White on
                setBlue(true);      //Blue on
                break;
            case 0x07:
                setRed(true);       //All on
                setWhite(true);
                setBlue(true);
                break;
            default:
                setRed(false);      //All off
                setWhite(false);
                setBlue(false);
                break;
        }
    }
}
