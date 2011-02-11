/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.utilities;

/**
 * Exception handler class
 * @author Jeremy Germita
 */
public class ExceptionHandler {
    private Throwable m_excep;
    private String m_subsys;
    /**
     * Constructor
     * @param e The exception
     * @param subSysName The name of the subsystem that throws the exception
     */
    public ExceptionHandler(Throwable e, String subSysName) {
        m_excep = e;
        m_subsys = subSysName;
    }

    public void print() {
        m_excep.printStackTrace();
        System.out.println("Exception at " + m_subsys + " thrown at: " + System.currentTimeMillis());
    }
}
