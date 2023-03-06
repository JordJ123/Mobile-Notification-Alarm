package phidget;

import com.phidget22.PhidgetException;
import com.phidget22.VoltageInput;

/**
 * LCD with extended list of methods to make use easier.
 * @author Jordan Jones
 */
public class ExtendedForceSensor extends VoltageInput {

    //CONSTANTS
    private static final double BUTTON_PRESSURE = 0.1000;

    //Attributes
    private double previousVoltage;

    /**
     * Creates an extended force sensor.
     * @param serialNumber Serial number of the device
     * @param channel Channel of the output
     * @throws PhidgetException Thrown if error with the phidget
     */
    public ExtendedForceSensor(int serialNumber, int channel)
        throws PhidgetException {
        super();
        PhidgetHandler.handlePhidget(this, serialNumber, channel);
    }

    /**
     * Sets the previous voltage.
     * @param voltage Current voltage
     */
    private void setPreviousVoltage(double voltage) {
        this.previousVoltage = voltage;
    }

    /**
     * Gets the previous voltage.
     * @return Previous voltage
     */
    private double getPreviousVoltage() {
        return previousVoltage;
    }

    /**
     * Checks if the force sensor performed a click action.
     * @param voltage Current voltage
     * @return True if the force sensor performed a click action
     */
    public boolean clicked(double voltage) {
        boolean isClicked = getPreviousVoltage() != 0
            && voltage > BUTTON_PRESSURE;
        setPreviousVoltage(voltage);
        return isClicked;

    }

}
