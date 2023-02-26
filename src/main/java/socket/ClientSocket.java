package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Socket for a client.
 * @author Jordan Jones
 */
public class ClientSocket extends Socket {

    /**
     * Creates a socket for a client.
     * @param host Host name to connect to
     * @param port Port number to connect to
     * @throws IOException Throws if issues with input/output
     */
    public ClientSocket(String host, int port, String msg) throws IOException {
        super(port);
        java.net.Socket socket = new java.net.Socket(host, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out.println(msg);
        System.out.println(in.readLine());
        in.close();
        out.close();
        socket.close();
    }

}
