package base_domain;

public abstract class Labeled {
    private String id;

    public Labeled(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
