package components;

import com.phidget22.PhidgetException;
import phidget.ExtendedLCD;

/**
 * Device that displays the notifications.
 * @author Jordan Jones
 */
public class NotificationDisplay {

    //CONSTANTS
    private static final String MESSAGE = "%s New Notifications";

    //Attributes
    private ExtendedLCD lcd;

    /**
     * Creates the notification display.
     * @param lcdSerialNumber Serial number of the lcd phidget
     * @throws PhidgetException Thrown if error with a phidget
     */
    public NotificationDisplay(int lcdSerialNumber) throws PhidgetException {
        setLcd(new ExtendedLCD(lcdSerialNumber));
        displayNotifications(0);
    }

    /**
     * Sets the lcd phidget.
     * @param lcd Lcd phidget
     */
    private void setLcd(ExtendedLCD lcd) {
        this.lcd = lcd;
    }

    /**
     * Gets the lcd phidget.
     * @return Lcd phidget
     */
    private ExtendedLCD getLcd() {
        return lcd;
    }

    /**
     * Displays the information of the notifications onto the lcd screen.
     * @param number Number of notifications to display
     * @throws PhidgetException Thrown if error with a phidget
     */
    public void displayNotifications(int number) throws PhidgetException {
        String message;
        if (number != 1) {
            message = MESSAGE;
        } else {
            message = MESSAGE.substring(0, MESSAGE.length() - 1);
        }
        getLcd().writeText(String.format(message, number));
    }


}
