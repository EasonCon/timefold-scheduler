package domain_2;


import DataStruct.Operation;
import DataStruct.Resource.RenewableResource;
import DataStruct.Task;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.simple.SimpleScore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@PlanningSolution
public class Solver {
    @PlanningEntityProperty
    private Head head;

    @ValueRangeProvider
    @PlanningEntityCollectionProperty
    private List<Node> nodes;

    @PlanningScore
    private SimpleScore score;

    private List<Task> tasks;
    private List<RenewableResource> resources;
    private List<Operation> operations;

    public Solver() {
    }

    public void buildResourceNodeMap(){
    }


}
