package components;

import com.phidget22.PhidgetException;
import main.Main;
import phidget.ExtendedForceSensor;

import java.io.IOException;

/**
 * Creates the middle button object for the alarm.
 * @author Jordan Jones
 */
public class MiddleButton {

    //Attributes
    private ExtendedForceSensor forceSensor;

    /**
     * Creates the middle button.
     * @param forceSensorSerialNumber Serial number of the force sensor
     * @param forceSensorChannel Force sensor channel
     */
    public MiddleButton(int forceSensorSerialNumber, int forceSensorChannel)
        throws PhidgetException {
        setForceSensor(new ExtendedForceSensor(forceSensorSerialNumber,
            forceSensorChannel));
        enableDismissAll();
    }

    /**
     * Sets the force sensor.
     * @param forceSensor Force sensor
     */
    private void setForceSensor(ExtendedForceSensor forceSensor) {
        this.forceSensor = forceSensor;
    }

    /**
     * Gets the force sensor.
     * @return Force sensor
     */
    private ExtendedForceSensor getForceSensor() {
        return forceSensor;
    }

    /**
     * Sets the functionality of the force sensor to be the dismiss all option.
     */
    private void enableDismissAll() {
        getForceSensor().addVoltageChangeListener(event -> {
            if (getForceSensor().clicked(event.getVoltage())) {
                try {
                    Main.clearNotifications();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
