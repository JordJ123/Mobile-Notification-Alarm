package socket;

import java.io.IOException;
import java.net.ConnectException;

/**
 * Socket for a client.
 * @author Jordan Jones
 */
public class ClientSocket extends Socket {

    //Attributes
    private Thread socketThread;
    private Connection serverConnection;

    /**
     * Creates a socket for a client.
     * @param host Host name to connect to
     * @param port Port number to connect to
     * @param inputRunnable Code to run when there is an input
     */
    public ClientSocket(String host, int port, InputRunnable inputRunnable) {
        super(port);
        setSocketThread(new Thread(() -> {
            try {
                while (true) {
                    if (getServerConnection() == null
                        || !getServerConnection().isActive()) {
                        java.net.Socket socket;
                        while (true) {
                            try {
                                socket = new java.net.Socket(host, port);
                                break;
                            } catch (ConnectException ce) {
                                if (ce.getMessage().endsWith(
                                    "Connection refused: connect")) {
                                    //Host does not exist
                                } else {
                                    ce.printStackTrace();
                                }
                            }
                        }
                        setServerConnection(
                            new Connection(socket, inputRunnable, null));
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }));
        getSocketThread().start();
    }

    /**
     * Sets the socket thread.
     * @param socketThread Socket thread
     */
    private void setSocketThread(Thread socketThread) {
        this.socketThread = socketThread;
    }

    /**
     * Sets the server connection.
     * @param serverConnection Server connection
     */
    private void setServerConnection(Connection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * Gets the socket thread.
     * @return Socket thread
     */
    private Thread getSocketThread() {
        return socketThread;
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
    public boolean send(Object data) throws IOException {
        if (getServerConnection() != null && getServerConnection().isActive()) {
            return getServerConnection().send(data);
        }
        return false;
    }

    /**
     * Closes the socket.
     * @throws IOException Thrown if error closing the socket
     */
    public void close() throws IOException {
        getServerConnection().close();
        getSocketThread().stop();
    }

}
