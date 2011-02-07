/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.utilities;

/**
 *
 * @author Jeremy Germita
 */
public class ExceptionHandler {
    private Exception m_excep;
    public ExceptionHandler(Exception e, String subSysName) {
        m_excep = e;
    }

    public void print() {
        m_excep.printStackTrace();
    }
}
