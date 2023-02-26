package phidget;

import com.phidget22.Phidget;
import com.phidget22.PhidgetException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * Helps handle and set multiple phidgets.
 * @author Jordan Jones
 */
public class PhidgetHandler {

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
     * @return Set up phidget
     * @throws PhidgetException Thrown if error with the phidget
     */
    @Contract("_, _ -> param1")
    public static @NotNull Phidget handleWidget(@NotNull Phidget phidget,
        int serialNumber)
        throws PhidgetException {
        phidget.setDeviceSerialNumber(serialNumber);
        phidget.open(5000);
        getPhidgets().add(phidget);
        return phidget;
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
