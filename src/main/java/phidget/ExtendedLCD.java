package phidget;

import com.phidget22.LCD;
import com.phidget22.LCDFont;
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
        PhidgetHandler.handleWidget(this, serialNumber);
        setAutoFlush(true);
        setBacklight(0.5);
        setContrast(0.5);
    }

    /**
     * Writes text to the lcd.
     * @param text Text to write
     * @throws PhidgetException Thrown if error with the phidget
     */
    public void writeText(String text) throws PhidgetException {
        clear();
        writeText(LCDFont.DIMENSIONS_6X12, 0, 0, text);
    }

}
