package socket;

import com.phidget22.PhidgetException;

/**
 * Runnable to run when the server connects to a client.
 * @author Jordan Jones
 */
public interface OnDisconnectRunnable {

    /**
     * Code to be executed when the server connects to a client.
     */
    void run() throws PhidgetException;

}