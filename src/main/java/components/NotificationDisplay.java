package components;

import com.phidget22.LCDFont;
import com.phidget22.PhidgetException;
import main.Main;
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
     * @param slider Slider
     * @param lcd Lcd
     * @throws PhidgetException Thrown if error with a phidget
     */
    public NotificationDisplay(ExtendedSlider slider, ExtendedLCD lcd)
        throws PhidgetException {
        setLcd(lcd);
        setSlider(slider);
        adjustBrightness(getSlider().getVoltageRatio());
        getSlider().voltageRatioChangeListener(
            event -> {
                try {
                    adjustBrightness(event.getVoltageRatio());
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
     * Enables the number mode.
     * @throws PhidgetException Thrown if error with a phidget
     */
    public void enableNumberMode() throws PhidgetException {
        displayNotifications(Main.getNotifications().size());
        getLcd().writeText(LCDFont.DIMENSIONS_6X12, 4, 1, "Dismiss All");
    }

    /**
     * Enables the read mode.
     * @throws PhidgetException Thrown if error with a phidget
     */
    public void enableReadMode() throws PhidgetException {
        getLcd().clear();
        getLcd().writeText(LCDFont.DIMENSIONS_6X12, 0, 0, "Read");
    }

    /**
     * Enables the alarm mode.
     * @throws PhidgetException Thrown if error with a phidget
     */
    public void enableSettingsMode() throws PhidgetException {
        getLcd().clear();
        getLcd().writeText(LCDFont.DIMENSIONS_6X12, 0, 0, "Settings");
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

    /**
     * Adjusts the brightness of the lcd screen.
     * @throws PhidgetException Thrown if error with the lcd phidget
     */
    private void adjustBrightness(double brightness) throws PhidgetException {
        getLcd().setContrast(brightness);
        if (brightness <= 0.999) {
            getLcd().setBacklight(1);
        } else {
            getLcd().setBacklight(0);
        }
    }


}
