package components;

import com.phidget22.PhidgetException;
import main.Main;
import phidget.force.Button;

/**
 * Creates the left button object for the alarm.
 * @author Jordan Jones
 */
public class LeftButton extends Button {

    /**
     * Creates the left button.
     * @param serialNumber Serial number of the force sensor
     * @param channel Force sensor channel
     * @throws PhidgetException Thrown if error with the phidget
     */
    public LeftButton(int serialNumber, int channel)
        throws PhidgetException {
        super(serialNumber, channel);
    }

    /**
     * Sets the functionality of the force sensor to be the read select option.
     */
    public void enableSwitchDeviceDisplay() {
        buttonAction(() -> {
            try {
                Main.switchDeviceDisplay();
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the functionality of the force sensor to be the read select option.
     */
    public void enableReadModeSelect() {
        buttonAction(() -> {
            try {
                Main.setMode(Main.Mode.READ);
            } catch (PhidgetException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the functionality of the force sensor to be the number select option.
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
