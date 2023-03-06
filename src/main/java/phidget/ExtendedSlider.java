package phidget;

import com.phidget22.PhidgetException;
import com.phidget22.VoltageRatioInput;

/**
 * Slider with an extended list of methods.
 * @author Jordan Jones
 */
public class ExtendedSlider extends VoltageRatioInput {

    /**
     * Creates an extended slider.
     * @param serialNumber Serial number of the device
     * @param channel Channel of the output
     * @throws PhidgetException Thrown if error with the phidget
     */
    public ExtendedSlider(int serialNumber, int channel)
        throws PhidgetException {
        super();
        PhidgetHandler.handlePhidget(this, serialNumber, channel);
    }


}
