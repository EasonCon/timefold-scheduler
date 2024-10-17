package Domain;

import DataStruct.Labeled;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PlanningSolution
public class Scheduler extends Labeled {


    public Scheduler() {
        super(null);
    }
}
