package phidget;

import com.phidget22.PhidgetException;
import com.phidget22.VoltageInput;

/**
 * Creates a motion sensor.
 * @author Jordan Jones
 */
public class ExtendedMotionSensor extends VoltageInput {

    /**
     * Creates a motion sensor.
     * @param serialNumber Serial number of the device
     * @param channel Channel of the output
     * @throws PhidgetException Thrown if error with the phidget
     */
    public ExtendedMotionSensor(int serialNumber, int channel)
        throws PhidgetException {
        super();
        PhidgetHandler.handlePhidget(this, serialNumber, channel);
    }

}
