package mobile;

/**
 * Represents a device.
 * @author Jordan Jones
 */
public class Device {

    //Attributes
    private String id;
    private String name;
    private String location;

    /**
     * Creates a device
     */
    public Device() {
        id = "dj292miO";
        name = "Android Phone";
        location = "My House";
    }

    /**
     * Gets the id.
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name.
     * @return Name
     */
    public String getName() {
        return name;
    }

}
