import java.util.Objects;

/**
 * Notification that is sent.
 * @author Jordan Jones
 */
public class Notification {

    //Attributes
    private int id;

    /**
     * Creates a notification.
     * @param id ID of the notification
     */
    public Notification(int id) {
        setId(id);
    }

    /**
     * Sets the id.
     * @param id ID of the notification
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the id.
     * @return Id
     */
    private int getId() {
        return id;
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
        return getId() == that.getId();
    }

    /**
     * Gets the hashcode of the value.
     * @return Returns the value
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
