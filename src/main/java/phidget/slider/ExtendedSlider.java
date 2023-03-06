package phidget.slider;

import com.phidget22.*;
import phidget.PhidgetHandler;

/**
 * Slider with an extended list of methods.
 * @author Jordan Jones
 */
public class ExtendedSlider extends VoltageRatioInput {

    //CONSTANTS
    private static final double BOUND = 0.0005;

    //Attributes
    private double previousVoltageRatio;

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

    /**
     * Sets the previous voltage ratio.
     * @param voltageRatio Voltage ratio
     */
    public void setPreviousVoltageRatio(double voltageRatio) {
        this.previousVoltageRatio = voltageRatio;
    }

    /**
     * Gets the previous voltage ratio.
     * @return Previous voltage ratio
     */
    public double getPreviousVoltageRatio() {
        return previousVoltageRatio;
    }

    /**
     * Adds a voltage ratio change listener to the slider.
     * @param runnable Code to run for the listener
     */
    public void voltageRatioChangeListener(
        VoltageRatioChangeRunnable runnable) {
        addVoltageRatioChangeListener(event -> {
            double voltageRatio = event.getVoltageRatio();
            if (voltageRatio > getPreviousVoltageRatio() + BOUND
                || voltageRatio < getPreviousVoltageRatio() - BOUND) {
                runnable.run(event);
                setPreviousVoltageRatio(voltageRatio);
            }
        });
    }

}
