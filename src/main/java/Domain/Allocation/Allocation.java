package Domain.Allocation;

import DataStruct.Operation;
import Domain.Listen.PreviousAllocationRangeListener;
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
    private List<Allocation> predecessorsAllocations = new ArrayList<>();  // In craft path
    private List<Allocation> successorsAllocations = new ArrayList<>();
    private List<ResourceNode> allResources = new ArrayList<>();

    @ShadowVariable(variableListenerClass = PreviousAllocationRangeListener.class, sourceVariableName = "previous")
    @ValueRangeProvider(id = "previousRange")
    private List<AllocationOrResource> possiblePreviousAllocation = new ArrayList<>();

    // variables
    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED, valueRangeProviderRefs = {"previousRange"})
    private AllocationOrResource previous;

    @PlanningVariable(valueRangeProviderRefs = {"delayRange"})
    private Integer delay;  // minutes

    // shadow
    @AnchorShadowVariable(sourceVariableName = "previous")
    private ResourceNode resourceNode;

    @ShadowVariable(variableListenerClass = StartTimeListener.class, sourceVariableName = "previous")
    @ShadowVariable(variableListenerClass = StartTimeListener.class, sourceVariableName = "delay")
    private Long startTime;
    private Long endTime;


    // value range
    @ValueRangeProvider(id = "delayRange")
    public CountableValueRange<Integer> getDelayRange() {
        return ValueRangeFactory.createIntValueRange(0, 600, 30);
    }


    public Allocation() {
    }

    public int getTaskDelay() {
        return 1;
    }
}
