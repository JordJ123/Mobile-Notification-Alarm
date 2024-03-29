package components;

import com.phidget22.PhidgetException;
import main.Main;
import phidget.force.Button;

/**
 * Creates the right button object for the alarm.
 * @author Jordan Jones
 */
public class RightButton extends Button {

    /**
     * Creates the right button.
     * @param serialNumber Serial number of the force sensor
     * @param channel Force sensor channel
     * @throws PhidgetException Thrown if error with the phidget
     */
    public RightButton(int serialNumber, int channel)
        throws PhidgetException {
        super(serialNumber, channel);
    }

    /**
     * Sets the functionality of the force sensor to be the read select option.
     */
    public void enableExtraModeSelect() {
        buttonAction(() -> {
            try {
                Main.setMode(Main.Mode.DEVICES);
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the functionality of the force sensor to be the next n option
     */
    public void enableNextNotification() {
        buttonAction(() -> {
            try {
                Main.nextNotification();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * Sets the functionality of the force sensor to be the number option.
     */
    public void enableNumberModeSelect() {
        buttonAction(() -> {
            try {
                Main.setMode(Main.Mode.NUMBER);
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        });
    }

}
