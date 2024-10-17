package DataStruct;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Labeled {
    private String id;

    public Labeled(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
