package Domain;

import Domain.Allocation.Allocation;
import DataStruct.ResourceNode;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;

public class SchedulerConstraintProvider implements ai.timefold.solver.core.api.score.stream.ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
//                fixedPenalize(constraintFactory
                ensureValidResourceNode(constraintFactory),
                TaskDelayPenalize(constraintFactory)
        };
    }


    protected Constraint fixedPenalize(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .penalize(HardMediumSoftScore.ofSoft((int) (Math.random() * 100)))
                .asConstraint("Fixed penalty for each allocation");
    }

    protected Constraint ensureValidResourceNode(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .filter(allocation -> allocation.getOperation().getExecutionModes().stream()
                        .noneMatch(exec -> exec.getResourceRequirement().getResourceNode().getId().equals(allocation.getResourceNode().getId())))
                .penalize(HardMediumSoftScore.ONE_HARD)
                .asConstraint("Invalid resource node");
    }

    protected Constraint TaskDelayPenalize(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .filter(allocation -> allocation.getOperation().isLast() && allocation.getEndTime() > 20)
                .penalize(HardMediumSoftScore.ONE_HARD)
                .asConstraint("Allocation delay");
    }
}
