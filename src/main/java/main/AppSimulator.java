package main;

import com.google.gson.Gson;
import mobile.DeviceInfo;
import mobile.Notification;
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
    private static String HOSTNAME = "localhost";

    /**
     * Main method.
     * @param args Command line argument
     */
    public static void main(String[] args) {

        //Setup
        DeviceInfo deviceInfo = new DeviceInfo("dwudhwudh");
        ClientSocket socket = new ClientSocket(HOSTNAME,
            Socket.TEST_PORT, input -> {
                String[][] keys = new Gson().fromJson(input, String[][].class);
                for (String[] key : keys) {
                    if (deviceInfo.getDeviceUniqueId().equals(key[0])) {
                        System.out.println(key[1]);
                    }
                }
            });
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
                    buffer.add(new Notification(id, deviceInfo, isActive));
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
