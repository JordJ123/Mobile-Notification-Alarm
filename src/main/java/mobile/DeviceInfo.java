package mobile;

import java.util.Objects;

/**
 * Represents a deviceInfo.
 * @author Jordan Jones
 */
public class DeviceInfo {

    //Attributes
    private String deviceName;
    private String deviceUniqueId;
    private double deviceLat;
    private double deviceLong;
    private String deviceLastKnownAddress;
    private int numberOfNotifications;

    /**
     * Creates a deviceInfo
     */
    public DeviceInfo(String deviceUniqueId) {
        this.deviceUniqueId = deviceUniqueId;
        deviceName = "Android Phone";
        deviceLastKnownAddress = "My House";
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
    public String getDeviceUniqueId() {
        return deviceUniqueId;
    }

    /**
     * Gets the name.
     * @return Name
     */
    public String getName() {
        return deviceName;
    }

    /**
     * Gets the location.
     * @return Location
     */
    public String getDeviceLastKnownAddress() {
        return deviceLastKnownAddress;
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
        DeviceInfo that = (DeviceInfo) o;
        return getDeviceUniqueId().equals(that.getDeviceUniqueId());
    }

    /**
     * Hashcode of the object.
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDeviceUniqueId());
    }

    /**
     * Converts the object into to a string.
     * @return Object into a string
     */
    @Override
    public String toString() {
        return "Device{" +
            "deviceName='" + deviceName + '\'' +
            ", deviceUniqueId='" + deviceUniqueId + '\'' +
            ", deviceLat=" + deviceLat +
            ", deviceLong=" + deviceLong +
            ", deviceLastKnownAddress='" + deviceLastKnownAddress + '\'' +
            ", numberOfNotifications=" + numberOfNotifications +
            '}';
    }
}
