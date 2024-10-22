package Domain.Allocation;

import DataStruct.ExecutionMode;
import DataStruct.Operation;
import DataStruct.Resource.RenewableResource;
import Listen.PossiblePreviousListListener;
import Listen.StartTimeListener;
import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.valuerange.CountableValueRange;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeFactory;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.domain.variable.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@PlanningEntity
public class Allocation extends AllocationOrResource {
    @PlanningId private String id;
    private Operation operation;
    private List<Allocation> predecessorsAllocations;  // In craft path
    private List<Allocation> successorsAllocations;
    private List<RenewableResource> resources; // All resources

    // variables
    @PlanningVariable(valueRangeProviderRefs = {"executionModes"})
    private ExecutionMode executionMode;

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED)
    private AllocationOrResource previous;

    @PlanningVariable(valueRangeProviderRefs = {"delayRange"})
    private int delay;  // minutes

    // shadow TODO: update previous range
    @ShadowVariable(variableListenerClass = PossiblePreviousListListener.class,sourceVariableName = "executionMode")
    private List<AllocationOrResource> possiblePreviousList;

    @AnchorShadowVariable(sourceVariableName = "previous")
    private AllocationOrResource resourceNode;

    @InverseRelationShadowVariable(sourceVariableName = "previous")
    private AllocationOrResource next;

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
        return ValueRangeFactory.createIntValueRange(0, 500, 30);
    }

    @ValueRangeProvider  // cover @Getter
    public List<AllocationOrResource> getPossiblePreviousList() {
        return possiblePreviousList;
    }


    public Allocation() {
    }
}
