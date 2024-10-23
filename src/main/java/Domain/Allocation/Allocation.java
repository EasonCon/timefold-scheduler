package Domain.Allocation;

import DataStruct.ExecutionMode;
import DataStruct.Operation;
import Domain.Listen.BaseReNewResourceListener;
import Domain.Listen.StartTimeListener;
import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.valuerange.CountableValueRange;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeFactory;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.domain.variable.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@PlanningEntity
public class Allocation extends AllocationOrResource {
    @PlanningId
    private String id;
    private Operation operation;
    private List<Allocation> predecessorsAllocations;  // In craft path
    private List<Allocation> successorsAllocations;
    private List<ResourceNode> allResources;

    // variables
    @PlanningVariable(valueRangeProviderRefs = {"executionModes"})
    private ExecutionMode executionMode;

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED, valueRangeProviderRefs = {"resourceNodesRange", "previousRange"})
    private AllocationOrResource previous;

    @PlanningVariable(valueRangeProviderRefs = {"delayRange"})
    private int delay;  // minutes

    // shadow TODO: update previous range
    @ShadowVariable(variableListenerClass = BaseReNewResourceListener.class, sourceVariableName = "executionMode")
    private ResourceNode baseResource;

    @AnchorShadowVariable(sourceVariableName = "previous")
    private ResourceNode resourceNode;

    @ShadowVariable(variableListenerClass = StartTimeListener.class, sourceVariableName = "previous")
    @ShadowVariable(variableListenerClass = StartTimeListener.class, sourceVariableName = "executionMode")
    @ShadowVariable(variableListenerClass = StartTimeListener.class, sourceVariableName = "delay")
    private long startTime;
    private long endTime;


    // value range
    @ValueRangeProvider(id = "executionModes")
    public List<ExecutionMode> getExecutionModes() {
        return operation.getExecutionModes();
    }

    @ValueRangeProvider(id = "delayRange")
    public CountableValueRange<Integer> getDelayRange() {
        return ValueRangeFactory.createIntValueRange(0, 600, 30);
    }

    @ValueRangeProvider(id = "previousRange")
    public List<Allocation> getAllocationRange() {
        List<Allocation> allocations = new ArrayList<>();
        for (ResourceNode resource : this.allResources) {
            Allocation cursor = resource.getNext();
            while (cursor.getNext() != null) {
                allocations.add(cursor);
                cursor = cursor.getNext();
            }
        }
        return allocations;
    }

    public Allocation() {
    }
}
