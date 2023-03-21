package socket;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a connection to a destination socket.
 * @author Jordan Jones
 */
public class Connection {

    //CONSTANTS
    private static final long SEND_WAIT = 3000;

    //Attributes
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private ReentrantReadWriteLock socketLock = new ReentrantReadWriteLock();


    /**
     * Creates a connection object.
     * @param socket Socket that is having a connection to
     * @throws IOException Thrown if error with the socket input
     */
    public Connection(@NotNull Socket socket, InputRunnable inputRunnable)
        throws IOException {
        setSocket(socket);
        setInput(new BufferedReader(new InputStreamReader(
            socket.getInputStream())));
        setOutput(new PrintWriter(socket.getOutputStream(), true));
        new Thread(() -> {
            while (true) {
                try {
                    socketLock.writeLock().lock();
                    if (getInput().ready()) {
                        String message = getInput().readLine();
                        if (message != null) {
                            if (!message.equals("ack")) {
                                getOutput().println("ack");
                                inputRunnable.run(message);
                            }
                        } else {
                            break; //Client socket is closed
                        }
                    }
                    socketLock.writeLock().unlock();
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
     * Sends the data over the server connection.
     * @param data Data
     * @return True if the send sent was a success
     */
    public boolean send(Object data) throws IOException {
        socketLock.writeLock().lock();
        if (isActive()) {
            getOutput().println(new Gson().toJson(data));
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime <= SEND_WAIT) {
                if (getInput().ready()) {
                    if (getInput().readLine().equals("ack")) {
                        socketLock.writeLock().unlock();
                        return true;
                    }
                }
            }
        }
        socketLock.writeLock().unlock();
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
