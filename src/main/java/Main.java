import com.google.gson.Gson;
import com.phidget22.PhidgetException;
import components.NotificationDisplay;
import phidget.ExtendedBuzzer;
import phidget.PhidgetHandler;
import socket.ServerSocket;
import socket.Socket;
import java.io.IOException;
import java.util.HashSet;

/**
 * Main entry of the program.
 * @author Jordan Jones
 */
public class Main {

    //SERIAL NUMBERS
    private static final int LCD_SERIAL_NUMBER = 39834;
    private static final int BUZZER_CHANNEL = 7;

    //VALUES
    private static final long BUZZ_DURATION = 3000;

    //Static Attributes
    private static HashSet<Notification> notifications = new HashSet<>();

    /**
     * Gets the notifications.
     * @return Notifications
     */
    private static HashSet<Notification> getNotifications() {
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

        //Adds the components
        ExtendedBuzzer buzzer = new ExtendedBuzzer(LCD_SERIAL_NUMBER,
            BUZZER_CHANNEL);
        NotificationDisplay notificationDisplay = new NotificationDisplay(
            LCD_SERIAL_NUMBER);

        //Sets the code for the alarm
        ServerSocket serverSocket = new ServerSocket(Socket.TEST_PORT,
            message -> {
            try {
                int previousSize = getNotifications().size();
                Notification[][] notifications
                    = new Gson().fromJson(message, Notification[][].class);
                for (Notification notification : notifications[0]) {
                    getNotifications().add(notification);
                }
                for (Notification notification : notifications[1]) {
                    getNotifications().remove(notification);
                }
                int newSize = getNotifications().size();
                notificationDisplay.displayNotifications(newSize);
                if (previousSize < newSize) {
                    buzzer.buzz(BUZZ_DURATION);
                }
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        });

        //Removes the components
        System.in.read();
        PhidgetHandler.closeAllPhidgets();
        serverSocket.close();

    }

}