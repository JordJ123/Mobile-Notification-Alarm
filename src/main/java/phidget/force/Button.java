package phidget.force;

import com.phidget22.PhidgetException;
import com.phidget22.VoltageInput;
import com.phidget22.VoltageInputVoltageChangeListener;
import phidget.PhidgetHandler;

/**
 * LCD with extended list of methods to make use easier.
 * @author Jordan Jones
 */
public class Button extends VoltageInput {

    //CONSTANTS
    private static final double BUTTON_PRESSURE = 0.01;

    //Attributes
    private VoltageInputVoltageChangeListener currentVIVCL;
    private double previousVoltage;

    /**
     * Creates an extended force sensor.
     * @param serialNumber Serial number of the deviceInfo
     * @param channel Channel of the output
     * @throws PhidgetException Thrown if error with the phidget
     */
    public Button(int serialNumber, int channel)
        throws PhidgetException {
        super();
        PhidgetHandler.handlePhidget(this, serialNumber, channel);
    }

    /**
     * Sets the current voltage input voltage change listener
     * @param vivcl Voltage input voltage change listener
     */
    public void setCurrentVIVCL(VoltageInputVoltageChangeListener vivcl) {
        this.currentVIVCL = vivcl;
    }

    /**
     * Sets the previous voltage.
     * @param voltage Current voltage
     */
    private void setPreviousVoltage(double voltage) {
        this.previousVoltage = voltage;
    }

    /**
     * Gets the current voltage input voltage change listener.
     * @return Current voltage input voltage change listener
     */
    public VoltageInputVoltageChangeListener getCurrentVIVCL() {
        return currentVIVCL;
    }

    /**
     * Gets the previous voltage.
     * @return Previous voltage
     */
    private double getPreviousVoltage() {
        return previousVoltage;
    }

    /**
     * Adds a button action to the button.
     * @param buttonAction Button action
     */
    public void buttonAction(ButtonAction buttonAction) {
        if (getCurrentVIVCL() != null) {
            removeVoltageChangeListener(getCurrentVIVCL());
        }
        if (buttonAction != null) {
            setCurrentVIVCL(event -> {
                boolean isClicked = getPreviousVoltage() != 0
                    && event.getVoltage() > BUTTON_PRESSURE;
                setPreviousVoltage(event.getVoltage());
                if (isClicked) {
                    buttonAction.run();
                }
            });
            addVoltageChangeListener(getCurrentVIVCL());
        } else {
            setCurrentVIVCL(null);
        }
    }

}
