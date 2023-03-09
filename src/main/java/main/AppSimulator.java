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

    //CONSTANT
    private static String HOSTNAME = "192.168.1.175";

    /**
     * Main method.
     * @param args Command line argument
     */
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            ClientSocket socket = new ClientSocket(HOSTNAME,
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
