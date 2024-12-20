package Domain;

import DataStruct.Task;
import Domain.Allocation.Allocation;
import DataStruct.ResourceNode;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@PlanningSolution
public class Scheduler extends AbstractPerishable {
    @ValueRangeProvider(id = "resourceNodes")
    @ProblemFactCollectionProperty
    private List<ResourceNode> resourceNodes;

    @ValueRangeProvider(id = "allocations")
    @PlanningEntityCollectionProperty
    private List<Allocation> allocations;

    @PlanningScore
    private HardMediumSoftScore hardMediumSoftScore;
    private List<Task> tasks;

    private int startSchedulingTime = 0;
    private int frozenSeconds = 0;



    public Scheduler() {
    }

}


