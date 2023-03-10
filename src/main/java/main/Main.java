package main;

import com.google.gson.Gson;
import com.phidget22.PhidgetException;
import components.LeftButton;
import components.MiddleButton;
import components.NotificationDisplay;
import components.RightButton;
import org.jetbrains.annotations.NotNull;
import phidget.*;
import phidget.slider.ExtendedSlider;
import socket.ServerSocket;
import socket.Socket;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Main entry of the program.
 * @author Jordan Jones
 */
public class Main {

    //SERIAL NUMBERS
    private static final int LCD_SERIAL_NUMBER = 39834;
    private static final int BUTTON_1_CHANNEL = 7;
    private static final int BUTTON_2_CHANNEL = 6;
    private static final int BUTTON_3_CHANNEL = 5;
    private static final int BUZZER_CHANNEL = 7;
    private static final int LED_CHANNEL = 0;
    private static final int MOTION_CHANNEL = 4;
    private static final int SLIDER_CHANNEL = 0;
    private static final int VIBRATOR_CHANNEL = 1;

    //VALUES
    private static final long BUZZ_DURATION = 3000;

    //Static Attributes
    private static Mode mode = Mode.NUMBER;
    private static HashSet<Notification> notifications = new HashSet<>();
    private static LeftButton leftButton;
    private static MiddleButton middleButton;
    private static RightButton rightButton;
    private static ExtendedBuzzer buzzer;
    private static ExtendedLED led;
    private static NotificationDisplay notificationDisplay;
    private static ExtendedVibrator vibrator;
    private static ServerSocket serverSocket;
    private static ReentrantReadWriteLock readWriteLock
        = new ReentrantReadWriteLock();

    /**
     * Possible modes the alarm could be in.
     */
    public enum Mode {
        NUMBER, READ, SETTINGS
    }

    /**
     * Sets the mode.
     * @param mode Mode
     * @throws PhidgetException Thrown if error with a phidget
     */
    public static void setMode(Mode mode) throws PhidgetException {
        Main.mode = mode;
        switch (Main.mode) {
            case NUMBER:
                getLeftButton().enableReadModeSelect();
                getMiddleButton().enableDismissAll();
                getRightButton().enableSettingsModeSelect();
                getNotificationDisplay().enableNumberMode();
                break;
            case READ:
                getLeftButton().enableNumberModeSelect();
                getMiddleButton().buttonAction(null);
                getRightButton().buttonAction(null);
                getNotificationDisplay().enableReadMode();
                break;
            case SETTINGS:
                getLeftButton().buttonAction(null);
                getMiddleButton().buttonAction(null);
                getRightButton().enableNumberModeSelect();
                getNotificationDisplay().enableSettingsMode();
                break;
            default:
                throw new EnumConstantNotPresentException(Mode.class,
                    Main.mode.toString());
        }
    }

    /**
     * Sets the left button.
     * @param leftButton Left button
     */
    private static void setLeftButton(LeftButton leftButton) {
        Main.leftButton = leftButton;
    }

    /**
     * Sets the middle button.
     * @param middleButton Middle button
     */
    private static void setMiddleButton(MiddleButton middleButton) {
        Main.middleButton = middleButton;
    }

    /**
     * Sets the right button.
     * @param rightButton Right button
     */
    private static void setRightButton(RightButton rightButton) {
        Main.rightButton = rightButton;
    }

    /**
     * Sets the buzzer.
     * @param buzzer Buzzer
     */
    private static void setBuzzer(ExtendedBuzzer buzzer) {
        Main.buzzer = buzzer;
    }

    /**
     * Sets the LED.
     * @param led LED
     */
    private static void setLed(ExtendedLED led) {
        Main.led = led;
    }

    /**
     * Sets the notification display.
     * @param notificationDisplay Notification display
     */
    private static void setNotificationDisplay(
        NotificationDisplay notificationDisplay) {
        Main.notificationDisplay = notificationDisplay;
    }

    /**
     * Sets the vibrator.
     * @param vibrator Vibrator
     */
    private static void setVibrator(ExtendedVibrator vibrator) {
        Main.vibrator = vibrator;
    }

    /**
     * Sets the server socket.
     * @param serverSocket Server socket
     */
    private static void setServerSocket(ServerSocket serverSocket) {
        Main.serverSocket = serverSocket;
    }

    /**
     * Gets the mode.
     * @return Mode
     */
    private static Mode getMode() {
        return mode;
    }

    /**
     * Gets the left button.
     * @return Left button
     */
    private static LeftButton getLeftButton() {
        return leftButton;
    }

    /**
     * Gets the middle button.
     * @return Middle button
     */
    private static MiddleButton getMiddleButton() {
        return middleButton;
    }

    /**
     * Gets the right button.
     * @return Right button
     */
    private static RightButton getRightButton() {
        return rightButton;
    }

    /**
     * Gets the buzzer.
     * @return Buzzer
     */
    private static ExtendedBuzzer getBuzzer() {
        return buzzer;
    }

    /**
     * Gets the LED.
     * @return LED
     */
    private static ExtendedLED getLed() {
        return led;
    }

    /**
     * Gets the notification display.
     * @return Notification display
     */
    private static NotificationDisplay getNotificationDisplay() {
        return notificationDisplay;
    }

    /**
     * Gets the vibrator.
     * @return Vibrator
     */
    private static ExtendedVibrator getVibrator() {
        return vibrator;
    }

    /**
     * Gets the notifications.
     * @return Notifications
     */
    public static HashSet<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Gets the server socket.
     * @return Server socket
     */
    private static ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Gets the read write lock.
     * @return Read write lock
     */
    private static ReentrantReadWriteLock getReadWriteLock() {
        return readWriteLock;
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
        setLeftButton(new LeftButton(LCD_SERIAL_NUMBER, BUTTON_1_CHANNEL));
        setMiddleButton(new MiddleButton(LCD_SERIAL_NUMBER, BUTTON_2_CHANNEL));
        setRightButton(new RightButton(LCD_SERIAL_NUMBER, BUTTON_3_CHANNEL));
        setBuzzer(new ExtendedBuzzer(LCD_SERIAL_NUMBER, BUZZER_CHANNEL));
        setLed(new ExtendedLED(LCD_SERIAL_NUMBER, LED_CHANNEL));
        setNotificationDisplay(new NotificationDisplay(
            new ExtendedMotionSensor(LCD_SERIAL_NUMBER, MOTION_CHANNEL),
            new ExtendedSlider(LCD_SERIAL_NUMBER, SLIDER_CHANNEL),
            new ExtendedLCD(LCD_SERIAL_NUMBER)));
//        setVibrator(new ExtendedVibrator(LCD_SERIAL_NUMBER, VIBRATOR_CHANNEL));
        setMode(Mode.NUMBER);

        //Sets the code for the alarm
        setServerSocket(new ServerSocket(Socket.TEST_PORT,
            message -> {
            try {
                Notification notification
                    = new Gson().fromJson(message, Notification.class);
                updateNotifications(notification);
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        }));

        //Removes the components
        System.in.read();
        PhidgetHandler.closeAllPhidgets();
        getServerSocket().close();

    }

    /**
     * Updates the notifications with the given notification.
     * @throws PhidgetException Thrown if error with a phidget
     */
    private static void updateNotifications(
        @NotNull Notification notification) throws PhidgetException {
        getReadWriteLock().writeLock().lock();
        boolean isUpdated = false;
        if (notification.getIsActiveNotification()
            && !getNotifications().contains(notification)) {
            getNotifications().add(notification);
            isUpdated = true;
        } else if (!notification.getIsActiveNotification()
            && getNotifications().contains(notification)) {
            getNotifications().remove(notification);
            isUpdated = true;
        }
        if (isUpdated) {
            switch (getMode()) {
                case NUMBER:
                    getNotificationDisplay().displayNotifications(
                        getNotifications().size());
                    break;
                default:
                    throw new EnumConstantNotPresentException(Mode.class,
                        getMode().toString());
            }
            if (notification.getIsActiveNotification()) {
                getLed().setDutyCycle(1);
//                getVibrator().setDutyCycle(1);
                getBuzzer().buzz(BUZZ_DURATION);
                getLed().setDutyCycle(0);
//                getVibrator().setDutyCycle(0);
            }
        }
        getReadWriteLock().writeLock().unlock();
    }

    /**
     * Clears all the notifications.
     * @throws PhidgetException Thrown if error with a notification
     */
    public static void clearNotifications() throws PhidgetException {
        getReadWriteLock().writeLock().lock();
        if (getNotifications().size() > 0) {
            int[] ids = new int[getNotifications().size()];
            Notification[] notifications = getNotifications().toArray(
                new Notification[0]);
            for (int i = 0; i < getNotifications().size(); i++) {
                ids[i] = notifications[i].getId();
            }
            getNotifications().clear();
            getNotificationDisplay().displayNotifications(0);
            getServerSocket().send(ids);
        }
        getReadWriteLock().writeLock().unlock();
    }


}