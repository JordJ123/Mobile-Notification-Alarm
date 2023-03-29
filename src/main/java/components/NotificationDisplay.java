package components;

import com.phidget22.LCDFont;
import com.phidget22.PhidgetException;
import main.Main;
import main.Notification;
import phidget.ExtendedLCD;
import phidget.ExtendedMotionSensor;
import phidget.slider.ExtendedSlider;

/**
 * Device that displays the notifications.
 * @author Jordan Jones
 */
public class NotificationDisplay {

    //CONSTANTS
    private static final double MOTION_THRESHOLD = 0;
    private static final long ON_DURATION = 5000;
    private static final String DISPLAY_NOTIFICATIONS = "%s New Notifications";
    private static final String NO_NOTIFICATIONS = "No Notifications";

    //Attributes
    private ExtendedLCD lcd;
    private ExtendedMotionSensor motionSensor;
    private ExtendedSlider slider;
    private boolean isOn;
    private long onTime;

    /**
     * Creates the notification display.
     * @param motionSensor Motion sensor
     * @param slider Slider
     * @param lcd Lcd
     * @throws PhidgetException Thrown if error with a phidget
     */
    public NotificationDisplay(ExtendedMotionSensor motionSensor,
        ExtendedSlider slider, ExtendedLCD lcd)
        throws PhidgetException {
        setLcd(lcd);
        setMotionSensor(motionSensor);
        setSlider(slider);
        adjustBrightness(1);
        getMotionSensor().addVoltageChangeListener(event -> {
            try {
                if (event.getVoltage() > MOTION_THRESHOLD) {
                    if (!getIsOn()) {
                        setIsOn(true);
                        adjustBrightness(getSlider().getVoltageRatio());
                    }
                    setOnTime(System.currentTimeMillis());
                } else {
                    if (onTimerFinished() && getIsOn()) {
                        setIsOn(false);
                        adjustBrightness(1);
                    }
                }
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        });
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
     * Sets the lcd.
     * @param lcd Lcd
     */
    private void setLcd(ExtendedLCD lcd) {
        this.lcd = lcd;
    }

    /**
     * Sets the motion sensor
     * @param motionSensor Motion sensor
     */
    private void setMotionSensor(ExtendedMotionSensor motionSensor) {
        this.motionSensor = motionSensor;
    }

    /**
     * Sets the slider.
     * @param slider Slider
     */
    private void setSlider(ExtendedSlider slider) {
        this.slider = slider;
    }

    /**
     * Sets if the screen is on.
     * @param isOn True if the screen is on
     */
    private void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    /**
     * Sets the time the device was last put on.
     * @param onTime the device was last put on
     */
    private void setOnTime(long onTime) {
        this.onTime = onTime;
    }

    /**
     * Gets the LCD.
     * @return LCD
     */
    private ExtendedLCD getLcd() {
        return lcd;
    }

    /**
     * Gets the motion sensor.
     * @return Motion sensor
     */
    private ExtendedMotionSensor getMotionSensor() {
        return motionSensor;
    }

    /**
     * Gets the slider.
     * @return Slider
     */
    private ExtendedSlider getSlider() {
        return slider;
    }

    /**
     * Gets if the screen is on.
     * @return Return if the screen is on
     */
    private boolean getIsOn() {
        return isOn;
    }

    /**
     * Get the time the device was last put on
     * @return Time the device was last put on
     */
    private long getOnTime() {
        return onTime;
    }

    /**
     * Enables the number mode.
     * @throws PhidgetException Thrown if error with a phidget
     */
    public void enableNumberMode() throws PhidgetException {
        displayNotifications(Main.getNotifications().size());
    }

    /**
     * Enables the alarm mode.
     * @throws PhidgetException Thrown if error with a phidget
     */
    public void enableExtraMode() throws PhidgetException {
        getLcd().displayText("Settings", true);
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
            message = DISPLAY_NOTIFICATIONS;
        } else {
            message = DISPLAY_NOTIFICATIONS.substring(
                0, DISPLAY_NOTIFICATIONS.length() - 1);
        }
        if (number > 9) {
            numberString = "9+";
        } else {
            numberString = Integer.toString(number);
        }
        getLcd().displayText(String.format(message, numberString), true);
    }

    /**
     * Display a given notification.
     * @param notification Notification to display
     */
    public void displayNotification(Notification notification) {
        if (notification != null) {
            getLcd().displayText(notification.getName(), true);
            getLcd().displayText(notification.getTitle(), false);
        } else {
            getLcd().displayText(NO_NOTIFICATIONS, true);
        }
    }

    /**
     * Adjusts the brightness of the lcd screen.
     * @throws PhidgetException Thrown if error with the lcd phidget
     */
    private synchronized void adjustBrightness(double brightness)
        throws PhidgetException {
        if (brightness <= 0.999 && getIsOn()) {
            getLcd().setBacklight(1);
            getLcd().setContrast(brightness);
        } else {
            getLcd().setBacklight(0);
            getLcd().setContrast(1);
        }
    }

    /**
     * Checks if the on timer has finished
     */
    private boolean onTimerFinished() {
        return System.currentTimeMillis() > getOnTime() + ON_DURATION;
    }


}
