package phidget;

import com.phidget22.DigitalOutput;
import com.phidget22.PhidgetException;

/**
 * Buzzer with extended list of methods.
 * @author Jordan Jones
 */
public class ExtendedBuzzer extends DigitalOutput {

    /**
     * Creates an extended buzzer.
     * @param serialNumber Serial number of the device
     * @param channel Channel of the output
     * @throws PhidgetException Thrown if error with the phidget
     */
    public ExtendedBuzzer(int serialNumber, int channel)
        throws PhidgetException {
        super();
        PhidgetHandler.handlePhidget(this, serialNumber, channel);
        setDutyCycle(0);
    }

    /**
     * Makes the buzzer make a buzzer sound
     * @param duration Duration to buzz for in milliseconds
     * @throws PhidgetException Thrown if error with a phidget.
     */
    public void buzz(long duration) throws PhidgetException {
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) <= duration) {
            setDutyCycle(1);
            setDutyCycle(0);
        }
    }


}
