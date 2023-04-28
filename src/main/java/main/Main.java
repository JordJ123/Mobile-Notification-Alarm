package main;

import com.google.gson.Gson;
import com.phidget22.PhidgetException;
import components.LeftButton;
import components.MiddleButton;
import components.NotificationDisplay;
import components.RightButton;
import mobile.Device;
import mobile.Notification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import phidget.*;
import phidget.slider.ExtendedSlider;
import socket.ServerSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
    private static boolean shouldDisplayDeviceName = true;
    private static LinkedHashSet<Notification> notifications
        = new LinkedHashSet<>();
    private static LinkedHashMap<String, Device> devices
        = new LinkedHashMap<>();
    private static int currentNotification = 0;
    private static int currentDevice = 0;
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
        NUMBER, READ, DEVICES
    }

    /**
     * Sets the mode.
     * @param mode Mode
     * @throws PhidgetException Thrown if error with a phidget
     */
    public static void setMode(Mode mode) throws PhidgetException {
        getReadWriteLock().writeLock().lock();
        Main.mode = mode;
        switch (Main.mode) {
            case NUMBER:
                getLeftButton().enableReadModeSelect();
                getMiddleButton().enableDismissAll();
                getRightButton().enableExtraModeSelect();
                getNotificationDisplay().displayNotifications(
                    getNotifications().size());
                break;
            case READ:
                setCurrentNotification(0);
                getLeftButton().enableNumberModeSelect();
                getMiddleButton().enableDismissNotification();
                getRightButton().enableNextNotification();
                Notification currentNotification = getNotification(
                    getCurrentNotification());
                getNotificationDisplay().displayNotification(
                    deviceIndex(currentNotification), currentNotification);
                break;
            case DEVICES:
                setShouldDisplayDeviceName(true);
                setCurrentDevice(0);
                getLeftButton().enableSwitchDeviceDisplay();
                getMiddleButton().enableNextDevice();
                getRightButton().enableNumberModeSelect();
                getNotificationDisplay().displayDevice(getCurrentDevice(),
                    getDevices().values().toArray(new Device[0])
                        [getCurrentDevice()], getShouldDisplayDeviceName());
                break;
            default:
                throw new EnumConstantNotPresentException(Mode.class,
                    Main.mode.toString());
        }
        getReadWriteLock().writeLock().unlock();
    }

    /**
     * Sets if the devices mode should display the device name.
     * @param shouldDisplayDeviceName True if device name should be displayed
     */
    private static void setShouldDisplayDeviceName(
        boolean shouldDisplayDeviceName) {
        Main.shouldDisplayDeviceName = shouldDisplayDeviceName;
    }

    /**
     * Sets the current notification.
     * @param index Index of the current notification.
     */
    private static void setCurrentNotification(int index) {
        Main.currentNotification = index;
    }

    /**
     * Sets the current device.
     * @param index Index of the current device
     */
    private static void setCurrentDevice(int index) {
        Main.currentDevice = index;
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
     * Gets the devices.
     * @return Devices
     */
    public static LinkedHashMap<String, Device> getDevices() {
        return devices;
    }

    /**
     * Gets the notifications of the given index.
     * @param index Index of the notification
     * @return Notification of the given index
     */
    private static @Nullable Notification getNotification(int index) {
        if (notifications.size() > 0) {
            return notifications.toArray(new Notification[]{})[index];
        } else {
            return null;
        }
    }

    /**
     * Gets the current notification index.
     * @return Current notification index
     */
    private static int getCurrentNotification() {
        return currentNotification;
    }

    /**
     * Gets the current device.
     * @return Current device
     */
    private static int getCurrentDevice() {
        return currentDevice;
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
     * Gets if the devices mode should display the device name.
     * @return True if device name should be displayed
     */
    private static boolean getShouldDisplayDeviceName() {
        return shouldDisplayDeviceName;
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
        setServerSocket(new ServerSocket(8888,
            message -> {
                try {
                    Notification notification
                        = new Gson().fromJson(message, Notification.class);
                    Device device = notification.getDevice();
                    System.out.println(notification);
                    devices.put(device.getId(), device);
                    updateNotifications(notification);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },
            () -> {
                getReadWriteLock().writeLock().lock();
                Main.getNotifications().clear();
                getReadWriteLock().writeLock().unlock();
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
        boolean isAdded = false;
        boolean isDeleted = false;
        int index = -1;
        if (notification.getIsActiveNotification()
            && !getNotifications().contains(notification)) {
            getNotifications().add(notification);
            isAdded = true;
        } else if (!notification.getIsActiveNotification()
            && getNotifications().contains(notification)) {
            ArrayList<Notification> notifications = new ArrayList<>(
                getNotifications());
            index = notifications.indexOf(notification);
            getNotifications().remove(notification);
            isDeleted = true;
        }
        if (isAdded || isDeleted) {
            switch (getMode()) {
                case NUMBER:
                    getNotificationDisplay().displayNotifications(
                        getNotifications().size());
                    break;
                case READ:
                    if (isAdded) {
                        if (getNotifications().size() == 1) {
                            Notification currentNotification
                                = getNotification(0);
                            getNotificationDisplay().displayNotification(
                                deviceIndex(currentNotification), currentNotification);
                        }
                    }
                    if (isDeleted) {
                        if (index <= getCurrentNotification()) {
                            if (getCurrentNotification() != 0) {
                                setCurrentNotification(
                                    getCurrentNotification() - 1);
                            }
                            if (getNotifications().size() != 0) {
                                Notification currentNotification
                                    = getNotification(getCurrentNotification());
                                getNotificationDisplay().displayNotification(
                                    deviceIndex(currentNotification),
                                    currentNotification);
                            } else {
                                getNotificationDisplay().displayNotification(-1,
                                    null);
                            }
                        }
                    }
                    break;
                case DEVICES:
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
     */
    public static void clearNotifications() {
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
        if (getNotifications().size() > 0) {
            setCurrentNotification(getCurrentNotification() + 1);
            if (getCurrentNotification() == getNotifications().size()) {
                setCurrentNotification(0);
            }
            Notification currentNotification = getNotification(
                getCurrentNotification());
            getNotificationDisplay().displayNotification(
                deviceIndex(currentNotification), currentNotification);
        } else {
            getNotificationDisplay().displayNotification(-1, null);
        }
        getReadWriteLock().writeLock().unlock();
    }

    /**
     * Dismisses the current notification.
     * @throws PhidgetException Thrown if error with the notification display
     */
    public static void dismissNotification() throws PhidgetException {
        getReadWriteLock().writeLock().lock();
        Notification notification = getNotification(getCurrentNotification());
        assert notification != null;
        getBuffer().add(notification.getKey());
        getNotifications().remove(notification);
        if (getCurrentNotification() != 0 &&
            getCurrentNotification() == getNotifications().size()) {
            setCurrentNotification(getCurrentNotification() - 1);
        }
        Notification currentNotification = getNotification(
            getCurrentNotification());
        getNotificationDisplay().displayNotification(
            deviceIndex(currentNotification), currentNotification);
        getReadWriteLock().writeLock().unlock();
    }

    public static void switchDeviceDisplay() throws PhidgetException {
        getReadWriteLock().writeLock().lock();
        setShouldDisplayDeviceName(!getShouldDisplayDeviceName());
        getNotificationDisplay().displayDevice(getCurrentDevice(),
            getDevices().values().toArray(new Device[0])
                [getCurrentDevice()], getShouldDisplayDeviceName());
        getReadWriteLock().writeLock().unlock();
    }

    /**
     * Sets the current device to the next device.
     * @throws PhidgetException Thrown if error with the notification display
     */
    public static void nextDevice() throws PhidgetException {
        getReadWriteLock().writeLock().lock();
        if (getDevices().size() > 0) {
            setCurrentDevice(getCurrentDevice() + 1);
            if (getCurrentDevice() == getDevices().size()) {
                setCurrentDevice(0);
            }
            getNotificationDisplay().displayDevice(getCurrentDevice(),
                getDevices().values().toArray(new Device[0])
                    [getCurrentDevice()], getShouldDisplayDeviceName());

        } else {
            getNotificationDisplay().displayDevice(-1, null, false);
        }
        getReadWriteLock().writeLock().unlock();
    }

    /**
     * Gets the device index of the notification.
     * @return Device index
     */
    public static int deviceIndex(Notification notification) {
        if (notification != null) {
            return new ArrayList<>(devices.values()).indexOf(
                notification.getDevice());
        } else {
            return -1;
        }
    }

}