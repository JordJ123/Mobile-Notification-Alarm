package socket;

import com.phidget22.PhidgetException;

/**
 * Runnable to run when the server gets an input.
 * @author Jordan Jones
 */
public interface InputRunnable {

    /**
     * Code to be executed when the server gets an input.
     */
    void run(String input) throws PhidgetException;

}