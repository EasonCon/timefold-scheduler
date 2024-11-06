package Domain;

import Domain.Allocation.Allocation;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;

import java.util.HashSet;
import java.util.Set;

public class SchedulerConstraintProvider implements ai.timefold.solver.core.api.score.stream.ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                validResourceNodeHardConstraint(constraintFactory),
                TaskDelayPenalizeMediumConstraint(constraintFactory),
                NonvolatilePenalizeMediumConstraint(constraintFactory),
                CraftConstraintInOneResource(constraintFactory)
        };
    }

    protected Constraint validResourceNodeHardConstraint(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .filter(allocation -> allocation.getOperation().getExecutionModes().stream()
                        .noneMatch(exec -> exec.getResourceRequirement().getResourceNode().getId().equals(allocation.getResourceNode().getId())))
                .penalize(HardMediumSoftScore.ONE_HARD)
                .asConstraint("Invalid resource node");
    }

    protected Constraint TaskDelayPenalizeMediumConstraint(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .filter(allocation -> allocation.getOperation().isLast() && allocation.getEndTime() > 20)
                .penalize(HardMediumSoftScore.ONE_HARD)
                .asConstraint("Allocation delay");
    }

    protected Constraint NonvolatilePenalizeMediumConstraint(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .filter(allocation -> allocation.getOperation().getPlannedResource() != null
                        && !allocation.getResourceNode().getId().equals(allocation.getOperation().getPlannedResource().getId()))
                .penalize(HardMediumSoftScore.ONE_MEDIUM)
                .asConstraint("Nonvolatile penalize");
    }

    // TODO:这个约束还存在问题，不能满足有向图的约束，但是可以保证资源链路上不存在冲突
    protected Constraint CraftConstraintInOneResource(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .filter(allocation -> {
                    Set<Allocation> predecessorsAllocations = new HashSet<>(allocation.getPredecessorsAllocations());                    Allocation current = allocation.getNext();
                    while (current != null) {
                        if (predecessorsAllocations.contains(current)) {
                            return true;
                        }
                        current = current.getNext();
                    }
                    return false;
                })
                .penalize(HardMediumSoftScore.ONE_HARD)
                .asConstraint("Circular dependency hard constraint");
    }


}
