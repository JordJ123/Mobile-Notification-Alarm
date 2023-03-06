package phidget;

import com.phidget22.DigitalOutput;
import com.phidget22.PhidgetException;

/**
 * Buzzer with extended list of methods.
 * @author Jordan Jones
 */
public class ExtendedLED extends DigitalOutput {

    /**
     * Creates a buzzer.
     * @param serialNumber Serial number of the device
     * @param channel Channel of the output
     * @throws PhidgetException Thrown if error with the phidget
     */
    public ExtendedLED(int serialNumber, int channel)
        throws PhidgetException {
        super();
        PhidgetHandler.handlePhidget(this, serialNumber, channel);
        setDutyCycle(0);
    }


}
