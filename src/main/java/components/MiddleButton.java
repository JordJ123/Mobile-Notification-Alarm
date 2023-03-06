package components;

import com.phidget22.PhidgetException;
import main.Main;
import phidget.force.Button;

/**
 * Creates the middle button object for the alarm.
 * @author Jordan Jones
 */
public class MiddleButton extends Button {

    /**
     * Creates the middle button.
     * @param serialNumber Serial number of the force sensor
     * @param channel Force sensor channel
     * @throws PhidgetException Thrown if error with the phidget
     */
    public MiddleButton(int serialNumber, int channel) throws PhidgetException {
        super(serialNumber, channel);
    }

    /**
     * Sets the functionality of the force sensor to be the dismiss all option.
     */
    public void enableDismissAll() {
        buttonAction(() -> {
            try {
                Main.clearNotifications();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
