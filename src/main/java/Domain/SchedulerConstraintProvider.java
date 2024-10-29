package Domain;

import Domain.Allocation.Allocation;
import Domain.Allocation.ResourceNode;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.Joiners;

public class SchedulerConstraintProvider implements ai.timefold.solver.core.api.score.stream.ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
//                theSameResource(constraintFactory),
                fixedPenalize(constraintFactory)
        };
    }


    protected Constraint theSameResource(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(ResourceNode.class)
                .join(Allocation.class,
                        Joiners.equal(ResourceNode::getId, Allocation::getId))
                .groupBy((resourceNode, allocation) -> resourceNode,
                        ConstraintCollectors.countBi())
                .filter((resourceNode, allocationCount) -> allocationCount > 1)
                .penalize(HardMediumSoftScore.ofHard(10),
                        (resourceNode, allocationCount) -> allocationCount - 1)
                .asConstraint("theSameResource1");
    }

    protected Constraint fixedPenalize(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Allocation.class)
                .penalize(HardMediumSoftScore.ofHard((int) (Math.random() * 100)))
                .asConstraint("Fixed penalty for each allocation");
    }
}
