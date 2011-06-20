/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.utilities;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 * Exception handler class
 * @author Jeremy Germita
 */
public class ExceptionHandler {
    private Throwable m_excep;
    private String m_subsys;

    private DriverStationLCD lcd;
    /**
     * Constructor
     * @param e The exception
     * @param subSysName The name of the subsystem that throws the exception
     */
    public ExceptionHandler(Throwable e, String subSysName) {
        m_excep = e;
        m_subsys = subSysName;
        lcd = DriverStationLCD.getInstance();
    }

    public void print() {
        //m_excep.printStackTrace();
        //System.out.println("Exception at " + m_subsys + " thrown at: " + System.currentTimeMillis());
        lcd.println(DriverStationLCD.Line.kUser2, 0, "Exception Thrown at " + m_subsys);
    }
}
