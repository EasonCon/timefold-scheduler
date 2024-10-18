package DataStruct;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Labeled {
    @PlanningId
    private String id;

    public Labeled(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
