package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Socket for a server.
 * @author Jordan Jones
 */
public class ServerSocket extends Socket {

    //Attributes
    private java.net.ServerSocket socket;

    /**
     * Creates a server socket.
     * @param port Port number that represents it
     * @throws IOException Throws if error with input/output
     */
    public ServerSocket(int port, ServerMessageRunnable serverMessageRunnable)
        throws IOException {
        super(port);
        setSocket(new java.net.ServerSocket(port));
        new Thread(() -> {
            try {
                while (true) {
                    java.net.Socket clientSocket = getSocket().accept();
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);
                    serverMessageRunnable.run(in.readLine());
                    out.println("success");
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Sets the socket.
     * @param socket Socket
     */
    private void setSocket(java.net.ServerSocket socket) {
        this.socket = socket;
    }

    /**
     * Gets the socket.
     * @return Socket
     */
    private java.net.ServerSocket getSocket() {
        return socket;
    }

    /**
     * Closes the socket.
     * @throws IOException Thrown if error closing the server socket.
     */
    public void close() throws IOException {
        getSocket().close();
    }

}
