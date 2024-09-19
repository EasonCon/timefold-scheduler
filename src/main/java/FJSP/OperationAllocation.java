package FJSP;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import ai.timefold.solver.core.api.domain.variable.PlanningVariableGraphType;
import ai.timefold.solver.core.api.domain.variable.ShadowVariable;
import base_domain.ExecutionMode;
import base_domain.Labeled;
import base_domain.Operation;
import solver.OperationAllocationVariableListener;

import java.time.LocalDateTime;
import java.util.List;

@PlanningEntity
public class OperationAllocation extends Labeled implements Allocation {
    private Operation operation;
    private ExecutionMode assignedExecutionMode;  // planning variable 1
    private List<ExecutionMode> executionModes;

    private Allocation previousOperationAllocation; // / planning variable 2
    private Allocation nextOperationAllocation; // / shadow variable

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public OperationAllocation() {
        super(null);
    }

    public OperationAllocation(String id) {
        super(id);
    }

    public OperationAllocation(String id, Operation operation, ExecutionMode assignedExecutionMode, List<ExecutionMode> executionModes, Allocation previousOperationAllocation, Allocation nextOperationAllocation) {
        super(id);
        this.operation = operation;
        this.assignedExecutionMode = assignedExecutionMode;
        this.executionModes = executionModes;
        this.previousOperationAllocation = previousOperationAllocation;
        this.nextOperationAllocation = nextOperationAllocation;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @PlanningVariable(valueRangeProviderRefs = "executionModes")
    public ExecutionMode getAssignedExecutionMode() {
        return assignedExecutionMode;
    }

    public void setAssignedExecutionMode(ExecutionMode assignedExecutionMode) {
        this.assignedExecutionMode = assignedExecutionMode;
    }

    @ValueRangeProvider(id = "executionModes")
    public List<ExecutionMode> getExecutionModes() {
        return executionModes;
    }

    public void setExecutionModes(List<ExecutionMode> executionModes) {
        this.executionModes = executionModes;
    }

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED, valueRangeProviderRefs = "operationAllocationRange")
    public Allocation getPreviousOperationAllocation() {
        return previousOperationAllocation;
    }

    public void setPreviousOperationAllocation(Allocation previousOperationAllocation) {
        this.previousOperationAllocation = previousOperationAllocation;
    }

    @ShadowVariable(variableListenerClass = OperationAllocationVariableListener.class, sourceVariableName = "previousOperationAllocation")
    @ShadowVariable(variableListenerClass = OperationAllocationVariableListener.class, sourceVariableName = "assignedExecutionMode")
    public Allocation getNextOperationAllocation() {
        return nextOperationAllocation;
    }

    public void setNextOperationAllocation(Allocation nextOperationAllocation) {
        this.nextOperationAllocation = nextOperationAllocation;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return this.getOperation().getName();
    }
}
