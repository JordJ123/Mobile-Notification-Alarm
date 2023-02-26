import com.google.gson.Gson;
import socket.ClientSocket;
import socket.Socket;
import java.io.IOException;

/**
 * Used to simulate the app when it is unavailable.
 * @author Jordan Jones
 */
public class AppSimulator {

    //CONSTANTS
    private static final Notification[] ADD_DATA = {};
    private static final Notification[] REMOVE_DATA = {};

    /**
     * Main method.
     * @param args Command line argument
     * @throws IOException Thrown if error sending data between sockets
     */
    public static void main(String[] args) throws IOException {
        new ClientSocket(Socket.TEST_HOSTNAME, Socket.TEST_PORT,
            new Gson().toJson(new Notification[][]{ADD_DATA, REMOVE_DATA}));
    }

}
