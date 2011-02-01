/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.sensors;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Follower wheel class
 * @author Jeremy Germita
 */
public class FollowerWheel {

    private Encoder m_encoder;

    /**
     * Constructor.
     * @param aPort Encoder A
     * @param bPort Encoder B
     */
    public FollowerWheel(int aPort, int bPort) {
        m_encoder = new Encoder(aPort, bPort);
    }

    /**
     * Get the encoder rate
     * @return the rate
     */
    public double getRate() {
        return m_encoder.getRate();
    }
    
    public void reset() {
        m_encoder.reset();
    }
    public double getDistance() {
        return m_encoder.getDistance();
    }
}
