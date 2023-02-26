import com.phidget22.PhidgetException;
import phidget.PhidgetHandler;
import java.io.IOException;

/**
 * Main entry of the program.
 * @author Jordan Jones
 */
public class Main {

    //CONSTANTS
    private static final int LCD_SERIAL_NUMBER = 39834;

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
        notificationDisplay.displayNotifications(3);
        System.in.read();
        PhidgetHandler.closeAllPhidgets();
    }

}