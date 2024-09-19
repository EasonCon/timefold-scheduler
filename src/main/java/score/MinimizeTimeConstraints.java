package score;

import FJSP.OperationAllocation;
import ai.timefold.solver.core.api.score.buildin.simple.SimpleScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;

public class MinimizeTimeConstraints implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                this.Minimize(constraintFactory)
        };

    }

    private Constraint Minimize(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(OperationAllocation.class)
                .filter(operationAllocation -> operationAllocation.getEndTime().isAfter(operationAllocation.getOperation().getParentTask().getDueDate()))
                .penalize(SimpleScore.ONE)
                .asConstraint("Minimize Time Constraints");
    }
}
