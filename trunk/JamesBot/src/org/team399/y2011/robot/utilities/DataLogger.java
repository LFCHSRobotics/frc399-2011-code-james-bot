/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team399.y2011.robot.utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.microedition.io.Connector;

/**
 *
 * @author kspoelstra
 */
public class DataLogger {

    private PrintStream m_printStream;
    private DataLogger m_instance;


    /**
     * Constructor
     * @param fileName the filename to write to
     */
    public DataLogger(String fileName) {
        try {
            OutputStream m_output = Connector.openOutputStream(fileName);
            m_printStream = new PrintStream(m_output);
        } catch(IOException ioe) {
            new ExceptionHandler(ioe, "DataLogger").print();
        }
    }

    /**
     * Get instance of datalogger
     * @param fileName the filename to write to
     * @return the instance of the DataLogger
     */
    public DataLogger getInstance(String fileName) {
        if(m_instance == null) {
            m_instance = new DataLogger(fileName);
        }
        return m_instance;
    }

    /**
     * Print data, followed by a newline character to the printstream and flush it
     * @param data the data to print
     */
    public void println(String data) {
        m_printStream.println(data);
        m_printStream.flush();
    }

    /**
     * Print data to the printStream and flush it
     * @param data
     */
    public void print(String data) {
        m_printStream.print(data);
        m_printStream.flush();
    }

    /**
     * Close the printStream
     */
    public void close() {
        m_printStream.close();
    }

}
