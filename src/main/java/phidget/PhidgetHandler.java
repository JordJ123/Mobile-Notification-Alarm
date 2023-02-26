package phidget;

import com.phidget22.Phidget;
import com.phidget22.PhidgetException;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * Helps handle and set multiple phidgets.
 * @author Jordan Jones
 */
public class PhidgetHandler {

    //CONSTANTS
    private static final int TIMEOUT = 5000;

    //Static Attributes
    private static ArrayList<Phidget> phidgets = new ArrayList<>();

    /**
     * Gets the current active phidgets.
     * @return Active phidgets
     */
    private static ArrayList<Phidget> getPhidgets() {
        return phidgets;
    }

    /**
     * Handles and sets-up the given phidget.
     * @param phidget Phidget to bee handled and set-up
     * @param serialNumber Serial number of the phidget
     * @throws PhidgetException Thrown if error with the phidget
     */
    public static void handlePhidget(@NotNull Phidget phidget,
        int serialNumber) throws PhidgetException {
        phidgetHandle(phidget, serialNumber);
    }

    /**
     * Handles and sets-up the given phidget that has a channel.
     * @param phidget Phidget to bee handled and set-up
     * @param serialNumber Serial number of the phidget
     * @param channel Channel of the output
     * @throws PhidgetException Thrown if error with the phidget
     */
    public static void handlePhidget(@NotNull Phidget phidget,
        int serialNumber, int channel) throws PhidgetException {
        phidget.setChannel(channel);
        phidgetHandle(phidget, serialNumber);
    }

    /**
     * Handles all phidgets.
     * @param phidget Phidget to bee handled and set-up
     * @param serialNumber Serial number of the phidget
     * @throws PhidgetException Thrown if error with the phidget
     */
    private static void phidgetHandle(@NotNull Phidget phidget,
        int serialNumber) throws PhidgetException {
        phidget.setDeviceSerialNumber(serialNumber);
        phidget.open(TIMEOUT);
        getPhidgets().add(phidget);
    }

    /**
     * Closes all the active phidgets.
     * @throws PhidgetException Thrown if error with a phidget
     */
    public static void closeAllPhidgets() throws PhidgetException {
        for (Phidget phidget : getPhidgets()) {
            phidget.close();
        }
        getPhidgets().clear();
    }

}
