package Domain;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractPerishable {
    @PlanningId
    protected String id;

    protected AbstractPerishable() {
    }

    protected AbstractPerishable(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getName().replaceAll(".*\\.", "") + "-" + id;
    }
}
