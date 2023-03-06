package components;

import com.phidget22.LCDFont;
import com.phidget22.LCDScreenSize;
import com.phidget22.PhidgetException;
import phidget.ExtendedLCD;
import phidget.ExtendedSlider;

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
     * @param lcd Lcd
     * @throws PhidgetException Thrown if error with a phidget
     */
    public NotificationDisplay(ExtendedLCD lcd)
        throws PhidgetException {
        setLcd(lcd);
        displayNotifications(0);
        getLcd().writeText(LCDFont.DIMENSIONS_6X12, 4, 1, "Dismiss All");
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
        String numberString;
        if (number != 1) {
            message = MESSAGE;
        } else {
            message = MESSAGE.substring(0, MESSAGE.length() - 1);
        }
        if (number > 9) {
            numberString = "9+";
        } else {
            numberString = Integer.toString(number);
        }
        getLcd().writeText(LCDFont.DIMENSIONS_6X12, 0, 0, "                    "
            + "                                                              ");
        getLcd().writeText(LCDFont.DIMENSIONS_6X12, 0, 0,
            String.format(message, numberString));
    }


}
