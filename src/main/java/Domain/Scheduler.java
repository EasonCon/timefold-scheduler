package Domain;

import Domain.Allocation.Allocation;
import Domain.Allocation.ResourceNode;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@PlanningSolution
public class Scheduler {
    @PlanningId
    private String id;
    private String name;

    @ProblemFactCollectionProperty
    private List<ResourceNode> resourceNodes;

    @PlanningEntityCollectionProperty
    private List<Allocation> allocations;

    @PlanningScore
    private HardMediumSoftScore hardMediumSoftScore;

    public Scheduler() {
    }

    public boolean dataCheck() {
        /*
         1.check the resource timeslots never null
         2.check the execution mode is never null
         3.no loop in craft path

         */
        return true;
    }
}


