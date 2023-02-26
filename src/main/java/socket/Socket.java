package socket;

/**
 * Class representing a basis for a type of socket.
 * @author Jordan Jones
 */
public abstract class Socket {

    //TEST
    public static final int TEST_PORT = 8888;
    public static final String TEST_HOSTNAME = "localhost";
    public static final String TEST_DATA = "Hello There!";

    //ERRORS
    private static final int MIN_PORT_NUMBER = 1024;
    private static final String PORT_NUMBER_ERROR = "Port number must be 1024 "
        + "or greater";

    /**
     * Checks the port number before creating a type of socket.
     * @param port Port number
     */
    public Socket(int port) {
        if (port < MIN_PORT_NUMBER) {
            throw new IllegalArgumentException(PORT_NUMBER_ERROR);
        }
    }

}
