package socket;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Socket for a server.
 * @author Jordan Jones
 */
public class ServerSocket extends Socket {

    //Attributes
    private java.net.ServerSocket serverSocket;
    private ArrayList<Connection> clientConnections = new ArrayList<>();

    /**
     * Creates a server socket.
     * @param port Port number that represents it
     * @param inputRunnable Code that runs the server gets input from a client
     * @param onDisconnectRunnable Code that runs when a client connects
     * @throws IOException Throws if error with input/output
     */
    public ServerSocket(int port, InputRunnable inputRunnable,
        OnDisconnectRunnable onDisconnectRunnable)
        throws IOException {
        super(port);
        setServerSocket(new java.net.ServerSocket(port));
        new Thread(() -> {
            while (true) {
                try {
                    getClientConnections().add(new Connection(
                        getServerSocket().accept(), inputRunnable,
                        onDisconnectRunnable));
                } catch (SocketException se) {
                    if (se.getMessage().endsWith("socket closed")) {
                        break; // Attempted socket to connect to is closed
                    } else {
                        se.printStackTrace();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Sets the socket.
     * @param serverSocket Socket
     */
    private void setServerSocket(java.net.ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Gets the socket.
     * @return Socket
     */
    private java.net.ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Gets the client connections.
     * @return Client connections
     */
    private ArrayList<Connection> getClientConnections() {
        return clientConnections;
    }

    /**
     * Sends the data over the socket.
     * @param data Data to send
     * @return True if the data was sent to at least one client
     */
    public boolean send(Object data) throws IOException {
        boolean isSent = false;
        for (Connection clientConnection : getClientConnections()) {
            boolean isSuccess = clientConnection.send(data);
            if (isSuccess) {
                isSent = true;
            }
        }
        return isSent;
    }

    /**
     * Closes the socket.
     * @throws IOException Thrown if error closing the server socket.
     */
    public void close() throws IOException {
        for (Connection connection : getClientConnections()) {
            connection.close();
        }
        getServerSocket().close();
    }

}
