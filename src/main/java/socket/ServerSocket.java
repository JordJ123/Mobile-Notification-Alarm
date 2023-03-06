package socket;

import java.io.IOException;
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
     * @throws IOException Throws if error with input/output
     */
    public ServerSocket(int port,
        InputRunnable inputRunnable)
        throws IOException {
        super(port);
        setServerSocket(new java.net.ServerSocket(port));
        new Thread(() -> {
            try {
                while (true) {
                    getClientConnections().add(new Connection(
                        getServerSocket().accept(), inputRunnable));
                }
            } catch (IOException e) {
                e.printStackTrace();
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
     */
    public void send(Object data) {
        for (Connection clientConnection : getClientConnections()) {
            clientConnection.send(data);
        }
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
