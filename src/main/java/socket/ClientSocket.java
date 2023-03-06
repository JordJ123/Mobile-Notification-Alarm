package socket;

import java.io.IOException;

/**
 * Socket for a client.
 * @author Jordan Jones
 */
public class ClientSocket extends Socket {

    //Attributes
    private Connection serverConnection;

    /**
     * Creates a socket for a client.
     * @param host Host name to connect to
     * @param port Port number to connect to
     * @param inputRunnable Code to run when there is an input
     * @throws IOException Throws if issues with input/output
     */
    public ClientSocket(String host, int port, InputRunnable inputRunnable)
        throws IOException {
        super(port);
        setServerConnection(new Connection(new java.net.Socket(host, port),
            inputRunnable));
    }

    /**
     * Sets the server connection.
     * @param serverConnection Server connection
     */
    private void setServerConnection(Connection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * Gets the server connection.
     * @return Server connection
     */
    private Connection getServerConnection() {
        return serverConnection;
    }

    /**
     * Sends the data over the socket.
     * @param data Data to send
     */
    public void send(Object data) throws IOException {
        getServerConnection().send(data);
    }

    /**
     * Closes the socket.
     * @throws IOException Thrown if error closing the socket
     */
    public void close() throws IOException {
        getServerConnection().close();
    }

}
