package phidget.slider;

import com.phidget22.VoltageRatioInputVoltageRatioChangeEvent;

/**
 * Runnable for when a voltage ratio change event occurs.
 * @author Jordan Jones
 */
public interface VoltageRatioChangeRunnable {

    /**
     * Code that is run.
     * @param event Voltage ratio input change event
     */
    void run(VoltageRatioInputVoltageRatioChangeEvent event);

}
