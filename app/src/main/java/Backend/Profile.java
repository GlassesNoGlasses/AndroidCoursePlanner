package Backend;

public abstract class Profile {
    String id;

    public String getId() {
        return id;
    }
    public void setId (String id) {
        this.id = id;
    }

    abstract void generateView();
}
