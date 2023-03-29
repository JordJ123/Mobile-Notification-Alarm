package main;

import java.util.Objects;

/**
 * Notification that is sent.
 * @author Jordan Jones
 */
public class Notification {

    //Attributes
    private String id;
    private String key;
    private String appName;
    private String title;
    private String message;
    private boolean isActiveNotification;

    /**
     * Creates a notification.
     * @param id ID of the notification
     */
    public Notification(String id, boolean isActiveNotification) {
        setId(id);
        this.key = "key " + id;
        this.appName = "Facebook";
        this.title = "Title " + id + " and this is a very long title";
        this.message = "Message";
        this.isActiveNotification = isActiveNotification;
    }

    /**
     * Sets the id.
     * @param id ID of the notification
     */
    private void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the id.
     * @return Id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the key.
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the name.
     * @return Name
     */
    public String getName() {
        return appName;
    }

    /**
     * Gets the message.
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the title.
     * @return Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets if the notification is active.
     * @return Notification
     */
    public boolean getIsActiveNotification() {
        return isActiveNotification;
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
        Notification that = (Notification) o;
        return getId().equals(that.getId());
    }

    /**
     * Gets the hashcode of the value.
     * @return Returns the value
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * Returns a string version of the notification.
     * @return String version of the notification
     */
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() + ", " +
            "isActiveNotification=" + getIsActiveNotification() +
            '}';
    }
}
