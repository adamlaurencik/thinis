package sk.gymdb.thinis.gcm.web;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 17.1.2014
 * Time: 23:17
 */
public class Registration {

    private String id;
    private String clazz;

    public Registration(String id) {
        this.id = id;
    }

    public Registration(String id, String clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Registration)) return false;

        Registration that = (Registration) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
