package socket;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashSet;

/**
 * Represents a connection to a destination socket.
 * @author Jordan Jones
 */
public class Connection {

    //CONSTANTS
    private static final long SEND_WAIT = 10000;

    //Attributes
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private HashSet<Long> sendingIds = new HashSet<>();


    /**
     * Creates a connection object.
     * @param socket Socket that is having a connection to
     * @param inputRunnable Code that runs the server gets input from a client
     * @param onDisconnectRunnable Code that runs when a client disconnects
     * @throws IOException Thrown if error with the socket input
     */
    public Connection(@NotNull Socket socket, InputRunnable inputRunnable,
        OnDisconnectRunnable onDisconnectRunnable)
        throws IOException {
        setSocket(socket);
        setInput(new BufferedReader(new InputStreamReader(
            socket.getInputStream())));
        setOutput(new PrintWriter(socket.getOutputStream(), true));
        new Thread(() -> {
            while (true) {
                try {
                    String message = getInput().readLine();
                    if (message != null) {
                        if (!message.startsWith("ackId=")) {
                            String[] data = message.split(" sendingId=");
                            getOutput().println("ackId=" + data[1]);
                            inputRunnable.run(data[0]);
                        } else {
                            sendingIds.remove(Long.parseLong(
                                message.replace("ackId=", "")));
                        }
                    } else {
                        if (onDisconnectRunnable != null) {
                            onDisconnectRunnable.run();
                        }
                        break; //Client socket is closed
                    }
                } catch (SocketTimeoutException ste) {
                    //No input
                } catch (SocketException se) {
                    if (se.getMessage().endsWith("Connection reset")) {
                        try {
                            close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break; //Host Socket is closed
                    }
                    if (se.getMessage().endsWith("Socket closed")) {
                        break; //This socket is closed
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
     * Sets the socket that is connected to.
     * @param socket Socket that is connected to
     */
    private void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Sets the input.
     * @param input Input
     */
    private void setInput(BufferedReader input) {
        this.input = input;
    }

    /**
     * Sets the output.
     * @param output Output
     */
    private void setOutput(PrintWriter output) {
        this.output = output;
    }

    /**
     * Gets the socket that is connected to.
     * @return Socket that is connected to
     */
    private Socket getSocket() {
        return socket;
    }

    /**
     * Gets the input.
     * @return Input
     */
    private BufferedReader getInput() {
        return input;
    }

    /**
     * Gets the output.
     * @return Output
     */
    private PrintWriter getOutput() {
        return output;
    }

    /**
     * Gets sending ids
     * @return Gets the sending ids
     */
    private HashSet<Long> getSendingIds() {
        return sendingIds;
    }

    /**
     * Sends the data over the server connection.
     * @param data Data
     * @return True if sending was a success
     */
    public boolean send(Object data) {
        Long sendingId = System.currentTimeMillis();
        getSendingIds().add(sendingId);
        if (isActive()) {
            getOutput().println(new Gson().toJson(data)
                + " sendingId=" + sendingId);
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime <= SEND_WAIT) {
                if (!sendingIds.contains(sendingId)) {
                    return true;
                }
            }
        }
        getSendingIds().remove(sendingId);
        return false;
    }

    /**
     * Closes the connection.
     * @throws IOException Thrown if error closing the connection
     */
    public void close() throws IOException {
        if (isActive()) {
            getSocket().close();
        }
    }

    /**
     * Checks if the connection is active
     * @return True if the socket is active
     */
    public boolean isActive() {
        return !getSocket().isClosed();
    }

}
