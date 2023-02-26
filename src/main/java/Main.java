import com.phidget22.PhidgetException;
import components.NotificationDisplay;
import phidget.PhidgetHandler;
import socket.ServerSocket;
import socket.Socket;

import java.io.IOException;

/**
 * Main entry of the program.
 * @author Jordan Jones
 */
public class Main {

    //CONSTANTS
    private static final int LCD_SERIAL_NUMBER = 39834;

    //Static Attributes
    private static int notifications = 0;

    /**
     * Sets the notifications
     * @param notifications Notifications
     */
    private static void setNotifications(int notifications) {
        Main.notifications = notifications;
    }

    /**
     * Gets the notifications.
     * @return Notifications
     */
    private static int getNotifications() {
        return notifications;
    }

    /**
     * Main method.
     * @param args Command line arguments
     * @throws IOException Thrown if error with the system input to end program
     * @throws PhidgetException Thrown if error with a phidget
     */
    public static void main(String[] args)
        throws IOException, PhidgetException {
        NotificationDisplay notificationDisplay = new NotificationDisplay(
            LCD_SERIAL_NUMBER);
        ServerSocket serverSocket = new ServerSocket(Socket.TEST_PORT,
            message -> {
            try {
                int change = Integer.parseInt(message);
                setNotifications(getNotifications() + change);
                notificationDisplay.displayNotifications(getNotifications());
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        });
        System.in.read();
        PhidgetHandler.closeAllPhidgets();
        serverSocket.close();
    }

}