import socket.ClientSocket;
import socket.Socket;
import java.io.IOException;

/**
 * Used to simulate the app when it is unavailable.
 * @author Jordan Jones
 */
public class AppSimulator {

    //CONSTANTS
    private static final int TEST_DATA = 1;

    /**
     * Main method.
     * @param args Command line argument
     * @throws IOException Thrown if error sending data between sockets
     */
    public static void main(String[] args) throws IOException {
        new ClientSocket(Socket.TEST_HOSTNAME, Socket.TEST_PORT,
            Integer.toString(TEST_DATA));
    }

}
