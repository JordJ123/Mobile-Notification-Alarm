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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
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
    private static LinkedHashSet<Notification> notifications
        = new LinkedHashSet<>();
    private static int currentNotification = 0;
    private static ArrayList<String> buffer = new ArrayList<>();
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
        NUMBER, READ, EXTRA
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
                getRightButton().enableExtraModeSelect();
                getNotificationDisplay().enableNumberMode();
                break;
            case READ:
                getLeftButton().enableNumberModeSelect();
                getMiddleButton().enableNextNotification();
                getRightButton().enableDismissNotification();
                getNotificationDisplay().enableReadMode();
                break;
            case EXTRA:
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
     * Sets the current notification.
     * @param index Index of the current notification.
     */
    private static void setCurrentNotification(int index) {
        Main.currentNotification = index;
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
     * Gets the notifications.
     * @return Notifications
     */
    public static LinkedHashSet<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Gets the notifications of the given index.
     * @param index Index of the notification
     * @return Notification of the given index
     */
    private static Notification getNotification(int index) {
        return notifications.toArray(new Notification[]{})[index];
    }

    /**
     * Gets the current notification index.
     * @return Current notification index
     */
    private static int getCurrentNotification() {
        return currentNotification;
    }

    /**
     * Gets the notifications buffer.
     * @return Notifications buffer
     */
    public static ArrayList<String> getBuffer() {
        return buffer;
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
     */
    public static void main(String[] args) throws IOException, PhidgetException {

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
        setVibrator(new ExtendedVibrator(LCD_SERIAL_NUMBER, VIBRATOR_CHANNEL));
        setMode(Mode.NUMBER);

        //Sets the code for the buffer
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(100);
                    if (!getBuffer().isEmpty()) {
                        String[] keys = getBuffer().toArray(new String[0]);
                        if (getServerSocket().send(keys)) {
                            getBuffer().removeAll(Arrays.asList(keys));
                        }
                    }
                }
            } catch (IOException | InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();

        //Sets the code for the alarm
        setServerSocket(new ServerSocket(Socket.TEST_PORT,
            message -> {
                Notification notification
                    = new Gson().fromJson(message, Notification.class);
                System.out.println(notification);
                updateNotifications(notification);
            }
        ));

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
                case READ:
                case EXTRA:
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
            String[] keys = new String[getNotifications().size()];
            Notification[] notifications = getNotifications().toArray(
                new Notification[0]);
            for (int i = 0; i < getNotifications().size(); i++) {
                keys[i] = notifications[i].getKey();
            }
            getNotifications().clear();
            getNotificationDisplay().displayNotifications(0);
            getBuffer().addAll(Arrays.asList(keys));
        }
        getReadWriteLock().writeLock().unlock();
    }

    /**
     * Sets the current notification to the next notification.
     * @throws PhidgetException Thrown if error with the notification display
     */
    public static void nextNotification() throws PhidgetException {
        getReadWriteLock().writeLock().lock();
        next();
        getReadWriteLock().writeLock().unlock();
    }

    /**
     * Dismisses the current notification.
     * @throws PhidgetException Thrown if error with the notification display
     */
    public static void dismissNotification() throws PhidgetException {
        getReadWriteLock().writeLock().lock();
        Notification notification = getNotification(getCurrentNotification());
        getBuffer().add(notification.getKey());
        getNotifications().remove(notification);
        next();
        getReadWriteLock().writeLock().unlock();
    }

    /**
     * Sets the next notification.
     * @throws PhidgetException Thrown if error with the notification display
     */
    private static void next() throws PhidgetException {
        if (getNotifications().size() > 0) {
            setCurrentNotification(getCurrentNotification() + 1);
            if (getCurrentNotification() == getNotifications().size()) {
                setCurrentNotification(0);
            }
            getNotificationDisplay().displayNotification(
                getNotification(getCurrentNotification()));
        } else {
            getNotificationDisplay().displayNotification(null);
        }
    }

}