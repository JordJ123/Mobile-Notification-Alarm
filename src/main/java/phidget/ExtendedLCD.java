package phidget;

import com.phidget22.LCD;
import com.phidget22.LCDFont;
import com.phidget22.PhidgetException;

/**
 * LCD with extended list of methods to make use easier.
 * @author Jordan Jones
 */
public class ExtendedLCD extends LCD {

    //CONSTANTS
    private static final int COLUMN_COUNT = 20;
    private static final int LETTER_DURATION = 500;
    public static final String CLEAR_TEXT = "                    ";

    //Attributes
    private Thread[] lineThreads = {null, null};

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

    /**
     * Sets the line thread.
     * @param line Line the thread is for.
     * @param thread Thread for the line
     */
    public void setLineThread(int line, Thread thread) {
        if (lineThreads[line] != null) {
            lineThreads[line].stop();
        }
        lineThreads[line] = thread;
    }

    /**
     * Gets the line thread.
     * @param line Line the wanted thread is for
     * @return Line thread
     */
    public Thread getLineThread(int line) {
        return lineThreads[line];
    }

    /**
     * Displays the given text on the given line.
     * @param text Text to display
     * @param isFirstLine True if the first line, false fn the second
     */
    public void displayText(String text, boolean isFirstLine) {
        int line;
        if (isFirstLine) {
            line = 0;
        } else {
            line = 1;
        }
        setLineThread(line, new Thread(() -> {
            try {
                writeText(LCDFont.DIMENSIONS_6X12, 0, line, CLEAR_TEXT);
                if (text.length() > COLUMN_COUNT) {
                    while (true) {
                        for (int i = 0; i < text.length(); i++) {
                            int end = COLUMN_COUNT + i;
                            if (end > text.length()) {
                                end = text.length();
                            }
                            writeText(LCDFont.DIMENSIONS_6X12, 0, line,
                                CLEAR_TEXT);
                            writeText(LCDFont.DIMENSIONS_6X12, 0, line,
                                text.substring(i, end));
                            Thread.sleep(LETTER_DURATION);
                        }
                    }
                } else {
                    writeText(LCDFont.DIMENSIONS_6X12, 0, line, text);
                }
            } catch (PhidgetException | InterruptedException e) {
                e.printStackTrace();
            }
        }));
        getLineThread(line).start();
    }

    /**
     * Clears a line of text.
     * @param isFirstLine True if the first line is to be clear
     * @throws PhidgetException Thrown if error with the lcd
     */
    public void clearText(boolean isFirstLine) throws PhidgetException {
        int line;
        if (isFirstLine) {
            line = 0;
        } else {
            line = 1;
        }
        writeText(LCDFont.DIMENSIONS_6X12, 0, line, CLEAR_TEXT);
    }

}
