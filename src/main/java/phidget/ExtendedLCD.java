package phidget;

import com.phidget22.LCD;
import com.phidget22.PhidgetException;

/**
 * LCD with extended list of methods to make use easier.
 * @author Jordan Jones
 */
public class ExtendedLCD extends LCD {

    /**
     * Creates an extended lcd.
     * @param serialNumber Serial number of the lcd
     * @throws PhidgetException Thrown if error with the phidget
     */
    public ExtendedLCD(int serialNumber) throws PhidgetException {
        super();
        PhidgetHandler.handlePhidget(this, serialNumber);
        setAutoFlush(true);
        setBacklight(1);
        setContrast(0.5);
    }



}
