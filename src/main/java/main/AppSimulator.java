package main;

import socket.ClientSocket;
import socket.Socket;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Used to simulate the app when it is unavailable.
 * @author Jordan Jones
 */
public class AppSimulator {

    /**
     * Main method.
     * @param args Command line argument
     * @throws IOException Thrown if error sending data between sockets
     */
    public static void main(String[] args) throws IOException {
        try {
            Scanner in = new Scanner(System.in);
            ClientSocket socket = new ClientSocket(Socket.TEST_HOSTNAME,
                Socket.TEST_PORT, System.out::println);
            while (true) {
                System.out.print("Enter <add/remove> <id>: ");
                Scanner scanner = new Scanner(in.nextLine());
                String option = scanner.next();
                if (option.equals("add")) {
                    socket.send(new Notification(scanner.nextInt(), true));
                } else if (option.equals("remove")) {
                    socket.send(new Notification(scanner.nextInt(), false));
                } else if (option.equals("exit")) {
                    break;
                } else {
                    System.out.println("Please enter the correct option");
                }
            }
            socket.close();
        } catch (Exception exception) {
            System.exit(0);
        }
    }

}
