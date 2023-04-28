package main;

import mobile.Device;
import mobile.Notification;
import socket.ClientSocket;
import socket.Socket;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Used to simulate the app when it is unavailable.
 * @author Jordan Jones
 */
public class AppSimulator {

    //CONSTANT
    private static String HOSTNAME = "192.168.1.249";

    /**
     * Main method.
     * @param args Command line argument
     */
    public static void main(String[] args) {

        //Setup
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        Device device = new Device(Base64.getUrlEncoder().withoutPadding()
            .encodeToString(bytes).substring(0, 8));
        ClientSocket socket = new ClientSocket(HOSTNAME,
            Socket.TEST_PORT, System.out::println);
        Scanner in = new Scanner(System.in);
        ArrayList<Notification> buffer = new ArrayList<>();

        //Continuously tries to send notification data
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

        //This is where the notifications get posted
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
                    buffer.add(new Notification(id, device, isActive));
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
