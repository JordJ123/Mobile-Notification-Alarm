package socket;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents a connection to a client socket from a server socket.
 * @author Jordan Jones
 */
public class Connection {

    //Attributes
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    /**
     * Creates a client connection object.
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
                    inputRunnable.run(getInput().readLine());
                } catch (IOException e) {
                    e.printStackTrace();
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
     */
    public void send(Object data) {
        getOutput().println(new Gson().toJson(data));
    }

    /**
     * Closes the connection.
     * @throws IOException Thrown if error closing the connection
     */
    public void close() throws IOException {
        getInput().close();
        getOutput().close();
        getSocket().close();
    }

}
