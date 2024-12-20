package Domain.Allocation;

import DataStruct.ExecutionMode;
import DataStruct.Operation;
import DataStruct.OperationStartRelationShip;
import DataStruct.ResourceNode;
import Domain.GroupSchedulingFilter;
import Domain.Listen.StartTimeListener;
import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.entity.PlanningPin;
import ai.timefold.solver.core.api.domain.valuerange.CountableValueRange;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeFactory;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.domain.variable.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@PlanningEntity
public class Allocation extends AllocationOrResource {
    @NotNull(message = "Allocation must have an operation")
    private Operation operation;
    private List<Allocation> predecessorsAllocations = new ArrayList<>();  // In craft path
    private List<Allocation> successorsAllocations = new ArrayList<>();
    private List<ResourceNode> allResources = new ArrayList<>();
    private List<Allocation> allAllocations = new ArrayList<>();
    private List<AllocationOrResource> possiblePreviousAllocation;

    // variables
    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED, valueRangeProviderRefs = {"previousRange"})
    private AllocationOrResource previous;

//    @PlanningVariable(valueRangeProviderRefs = {"delayRange"})
//    private Integer delay;  // minutes

    // shadow
    @AnchorShadowVariable(sourceVariableName = "previous")
    private ResourceNode resourceNode;

    @ShadowVariable(variableListenerClass = StartTimeListener.class, sourceVariableName = "previous")
    private Long startTime;
    private Long endTime;


    // value range
    @ValueRangeProvider(id = "delayRange")
    public CountableValueRange<Integer> getDelayRange() {
        return ValueRangeFactory.createIntValueRange(0, 600, 30);
    }

    @ValueRangeProvider(id = "previousRange")
    public List<AllocationOrResource> getPossiblePreviousAllocation() {
        // Processing of frozen processes
        if (this.getOperation().isFrozen()) {
            if (this.getOperation().getFrozenPrevious() instanceof ResourceNode frozenPrevious) {
                return allResources.stream()
                        .filter(resourceNode -> resourceNode.getId().equals(frozenPrevious.getId()))
                        .collect(Collectors.toList());
            } else if (this.getOperation().getFrozenPrevious() instanceof Operation frozenPrevious) {
                return allAllocations.stream()
                        .filter(allocation -> allocation.getOperation().getId().equals(frozenPrevious.getId()))
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("FrozenPrevious is not a ResourceNode or Operation");
            }
        }
        List<ResourceNode> resourceList = new ArrayList<>();
        for (ExecutionMode executionMode : this.getOperation().getExecutionModes()) {
            String id = executionMode.getResourceRequirement().getResourceNode().getId();
            for (ResourceNode resourceNode : this.getAllResources()) {
                if (Objects.equals(resourceNode.getId(), id))
                    resourceList.add(resourceNode);
            }
        }
        List<Allocation> allocations = new ArrayList<>();
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
        allocations.sort(Comparator.nullsLast(Comparator.comparingLong(Allocation::getStartTime)));
        List<AllocationOrResource> allocationsOrResources = new ArrayList<>();
        allocationsOrResources.addAll(allocations);
        allocationsOrResources.addAll(resourceList);
        return allocationsOrResources;
    }

    public Long getEndTime() {
        ExecutionMode selectedExecutionMode = this.getOperation()
                .getExecutionModes()
                .stream()
                .filter(executionMode -> Objects.equals(
                        executionMode.getResourceRequirement().getResourceNode().getId(),
                        this.getResourceNode().getId()
                ))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No matching ExecutionMode found for ResourceNode ID: " + this.getResourceNode().getId()));
        return this.getResourceNode().getEndTime(this.getStartTime(), (long) (this.getOperation().getQuantity() * selectedExecutionMode.getBeat() / selectedExecutionMode.getQuantityPerBeat()));
    }


    public Long TimeConstraintFromResource() {
        // Time constraint from resource only considers post time which occupies the resource calendar.
        if (this.getPrevious() == null) {
            return null;
        } else if (this.getPrevious() instanceof ResourceNode) {
            return ((ResourceNode) this.getPrevious()).getTimeSlots().getFirst().getStart();
        } else {
            return this.getResourceNode().getEndTime(
                    ((Allocation) this.getPrevious()).getEndTime(),
                    ((Allocation) this.getPrevious()).getOperation().getResourceOccupiedPostTime());
        }
    }

    public Long TimeConstraintFromCraftPath() {
        // Time constraint from craft path considers non-resource occupied time.
        if (this.getPredecessorsAllocations().isEmpty()) {
            return 0L;  // First operation
        } else {
            List<Long> predecessorsTimeConstraints = new ArrayList<>();
            for (Allocation predecessor : this.getPredecessorsAllocations()) {
                Long predecessorEndTime = predecessor.getEndTime();
                if (this.getOperation().getOperationStartRelationShip().equals(OperationStartRelationShip.ES)) {
                    if (predecessorEndTime != null) {
                        predecessorsTimeConstraints.add(predecessor.getEndTime() + predecessor.getOperation().getNonResourceOccupiedPostTime());
                    } else {
                        return null;
                    }
                } else {
                    predecessorsTimeConstraints.add(predecessor.getStartTime());
                }
            }
            return predecessorsTimeConstraints.stream().max(Long::compare).orElse(null);
        }
    }

    @PlanningPin
    public boolean isPinned() {
        return false;
    }

    public Allocation() {
    }

}
