package Domain.Allocation;

import DataStruct.ExecutionMode;
import DataStruct.Operation;
import Listen.StartTimeListener;
import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.valuerange.CountableValueRange;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeFactory;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.domain.variable.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@PlanningEntity
public class Allocation extends AllocationOrResource {
    private Operation operation;

    // variables
    @PlanningVariable(valueRangeProviderRefs = {"executionModes"})
    private ExecutionMode executionMode;

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED)
    private AllocationOrResource previous;

    @PlanningVariable(valueRangeProviderRefs = {"delayRange"})
    private int delay;  // minutes

    // shadow
    @AnchorShadowVariable(sourceVariableName = "previous")
    private AllocationOrResource resourceNode;

    @InverseRelationShadowVariable(sourceVariableName = "previous")
    private AllocationOrResource next;
    
    @ShadowVariable(variableListenerClass = StartTimeListener.class, sourceVariableName = "previous")
    private long startTime;
    private long endTime;

    // value range

    @ValueRangeProvider(id = "executionModes")
    public List<ExecutionMode> getExecutionModes() {
        return operation.getExecutionModes();
    }

    @ValueRangeProvider(id = "delayRange")
    public CountableValueRange<Integer> getDelayRange() {
        return ValueRangeFactory.createIntValueRange(0, 500, 10);
    }


    public Allocation() {
    }
}
