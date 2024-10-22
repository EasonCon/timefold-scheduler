package domain_2;

import DataStruct.ExecutionMode;
import DataStruct.Operation;
import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.domain.variable.NextElementShadowVariable;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import ai.timefold.solver.core.api.domain.variable.PreviousElementShadowVariable;
import ai.timefold.solver.core.api.domain.variable.ShadowVariable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@PlanningEntity
public class Node {
    @PlanningId
    private String id;
    @PreviousElementShadowVariable(sourceVariableName = "nodes")
    private Node previousNode;
    @NextElementShadowVariable(sourceVariableName = "nodes")
    private Node nextNode;
    @PlanningVariable
    private ExecutionMode executionMode;
    @PlanningVariable
    private int delay;

    @ShadowVariable(sourceVariableName = "previousNode", variableListenerClass = NodeStartTimeListener.class)
    @ShadowVariable(sourceVariableName = "executionMode", variableListenerClass = NodeStartTimeListener.class)
    @ShadowVariable(sourceVariableName = "delay", variableListenerClass = NodeStartTimeListener.class)
    private Long startTime;
    private Long endTime;

    private Operation operation;
    private List<Node> processorsNodes;
    private List<Node> successorsNodes;

    @ValueRangeProvider
    public List<ExecutionMode> getExecutionModes() {
        return operation.getExecutionModes();
    }

    @ValueRangeProvider
    public List<Integer> getDelays() {
        return new ArrayList<>(List.of(10, 20, 30, 40, 50));  // In minutes
    }

    public Node() {
    }

    /*
     ***** Complex method *****
     */

    public Long getEndTime() {
        if (this.getStartTime() == null) {
            return null;
        } else {
            return this.getExecutionMode().getResourceRequirement().getRenewableResource().getEndTime(this.getStartTime(), this.getExecutionMode().getDuration());
        }
    }


}
