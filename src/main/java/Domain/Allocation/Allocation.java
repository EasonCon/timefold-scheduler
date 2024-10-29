package Domain.Allocation;

import DataStruct.ExecutionMode;
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
import java.util.Objects;

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
    private List<AllocationOrResource> possiblePreviousAllocation;
    private List<Allocation> allocations;

    // variables
    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED, valueRangeProviderRefs = {"resourceNodes","allocations"})
    private AllocationOrResource previous;

//    @PlanningVariable(valueRangeProviderRefs = {"delayRange"})
//    private Integer delay;  // minutes

    // shadow
    @AnchorShadowVariable(sourceVariableName = "previous")
    private ResourceNode resourceNode;

    //    @ShadowVariable(variableListenerClass = StartTimeListener.class, sourceVariableName = "previous")
    private Long startTime;
    private Long endTime;


    // value range
    @ValueRangeProvider(id = "delayRange")
    public CountableValueRange<Integer> getDelayRange() {
        return ValueRangeFactory.createIntValueRange(0, 600, 30);
    }

    @ValueRangeProvider(id = "previousRange")
    public List<AllocationOrResource> getPossiblePreviousAllocation() {
        List<ResourceNode> resourceList = new ArrayList<>();
        for (ExecutionMode executionMode : this.getOperation().getExecutionModes()) {
            String id = executionMode.getResourceRequirement().getResourceNode().getId();
            for (ResourceNode resourceNode : this.getAllResources()) {
                if (Objects.equals(resourceNode.getId(), id))
                    resourceList.add(resourceNode);
            }
        }
        List<AllocationOrResource> allocations = new ArrayList<>(resourceList);
        for (ResourceNode resourceNode : resourceList) {
            if (resourceNode.getNext() != null) {
                Allocation cursor = resourceNode.getNext();
                while (cursor != null) {
                    if (!Objects.equals(cursor.getId(), this.getId())) {
                        allocations.add(cursor);
                    }
                    cursor = cursor.getNext();
                }
            }
        }
        return allocations;
    }

    public Long getEndTime() {
        return this.getResourceNode().getEndTime(this.getStartTime(), 12L);
    }

    public Allocation() {
    }

}
