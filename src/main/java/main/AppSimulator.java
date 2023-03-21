package main;

import socket.ClientSocket;
import socket.Socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Used to simulate the app when it is unavailable.
 * @author Jordan Jones
 */
public class AppSimulator {

    //CONSTANT
    private static String HOSTNAME = "192.168.128.83";

    /**
     * Main method.
     * @param args Command line argument
     */
    public static void main(String[] args) {
        ClientSocket socket = new ClientSocket(Socket.TEST_HOSTNAME,
            Socket.TEST_PORT, System.out::println);
        Scanner in = new Scanner(System.in);
        ArrayList<Notification> buffer = new ArrayList<>();
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(100);
                    if (!buffer.isEmpty()) {
                        Notification notification
                            = buffer.get(buffer.size() - 1);
                        if (socket.send(notification)) {
                            buffer.remove(notification);
                        }
                    }
                }
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
        }).start();
        while (true) {
            try {
                System.out.print("Enter <add/remove> <id>: ");
                Scanner scanner = new Scanner(in.nextLine());
                String option = scanner.next();
                String id = scanner.next();
                boolean isActive = false;
                boolean canSend = false;
                if (option.equals("add")) {
                    isActive = true;
                    canSend = true;
                } else if (option.equals("remove")) {
                    isActive = false;
                    canSend = true;
                }
                if (canSend) {
                    buffer.add(new Notification(id, isActive));
                } else {
                    System.out.println(
                        "Please enter the correct option");
                }
            } catch (NoSuchElementException noSuchElementException) {
                System.out.println("Please enter the correct option");
            }
        }
    }

}
