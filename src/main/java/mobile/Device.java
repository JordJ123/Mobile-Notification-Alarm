package mobile;

import java.util.Objects;

/**
 * Represents a device.
 * @author Jordan Jones
 */
public class Device {

    //Attributes
    private String id;
    private String name;
    private String location;
    private int numberOfNotifications;

    /**
     * Creates a device
     */
    public Device(String id) {
        this.id = id;
        name = "Android Phone";
        location = "My House";
    }

    /**
     * Set number of notifications.
     * @param numberOfNotifications Number of notifications
     */
    public void setNumberOfNotifications(int numberOfNotifications) {
        this.numberOfNotifications = numberOfNotifications;
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

    /**
     * Gets the location.
     * @return Location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets number of notifications.
     * @return Number of notifications
     */
    public int getNumberOfNotifications() {
        return numberOfNotifications;
    }

    /**
     * Check if the objects are equal
     * @param o Object to compare to
     * @return True if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device that = (Device) o;
        return getId().equals(that.getId());
    }

    /**
     * Hashcode of the object.
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
