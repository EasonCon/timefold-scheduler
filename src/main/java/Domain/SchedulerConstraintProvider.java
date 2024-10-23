package Domain;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;

import java.util.Random;

public class SchedulerConstraintProvider implements ai.timefold.solver.core.api.score.stream.ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[0];
    }

    public SchedulerConstraintProvider() {
    }
}
