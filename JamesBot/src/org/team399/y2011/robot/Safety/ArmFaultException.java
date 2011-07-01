/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.Safety;

/**
 *
 * @author robotics
 */
public class ArmFaultException extends Exception{
    private String exception = "NONE";
    public ArmFaultException(String system) {
        exception = system;
    }

    public String getException() {
        return exception;
    }
}
