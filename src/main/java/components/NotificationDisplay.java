package components;

import com.phidget22.LCDFont;
import com.phidget22.PhidgetException;
import phidget.ExtendedLCD;
import phidget.slider.ExtendedSlider;

/**
 * Device that displays the notifications.
 * @author Jordan Jones
 */
public class NotificationDisplay {

    //CONSTANTS
    private static final String MESSAGE = "%s New Notifications";

    //Attributes
    private ExtendedLCD lcd;
    private ExtendedSlider slider;

    /**
     * Creates the notification display.
     * @param lcd Lcd
     * @param slider Slider
     * @throws PhidgetException Thrown if error with a phidget
     */
    public NotificationDisplay(ExtendedLCD lcd, ExtendedSlider slider)
        throws PhidgetException {
        setLcd(lcd);
        setSlider(slider);
        displayNotifications(0);
        getLcd().writeText(LCDFont.DIMENSIONS_6X12, 4, 1, "Dismiss All");
        getSlider().voltageRatioChangeListener(
            event -> {
                try {
                    System.out.println(event.getVoltageRatio());
                    getLcd().setContrast(event.getVoltageRatio());
                    if (event.getVoltageRatio() <= 0.999) {
                        getLcd().setBacklight(1);
                    } else {
                        getLcd().setBacklight(0);
                    }
                } catch (PhidgetException e) {
                    e.printStackTrace();
                }
            });
    }

    /**
     * Sets the lcd phidget.
     * @param lcd Lcd phidget
     */
    private void setLcd(ExtendedLCD lcd) {
        this.lcd = lcd;
    }

    /**
     * Sets the slider.
     * @param slider Slider
     */
    private void setSlider(ExtendedSlider slider) {
        this.slider = slider;
    }

    /**
     * Gets the LCD.
     * @return LCD
     */
    private ExtendedLCD getLcd() {
        return lcd;
    }

    /**
     * Gets the slider.
     * @return Slider
     */
    private ExtendedSlider getSlider() {
        return slider;
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
