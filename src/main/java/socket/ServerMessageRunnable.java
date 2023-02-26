package socket;

/**
 * Runnable to run when the server gets a message.
 */
public interface ServerMessageRunnable {

    /**
     * Code to be executed when the server get a message.
     */
    void run(String message);

}