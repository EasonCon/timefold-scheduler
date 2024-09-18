package domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import ai.timefold.solver.core.api.domain.variable.PlanningVariableGraphType;
import ai.timefold.solver.core.api.domain.variable.ShadowVariable;
import base_domain.Labeled;
import base_domain.Task;
import solver.NextNodeListener;

import java.time.LocalDateTime;
import java.util.List;

@PlanningEntity
public class SequenceNode extends Labeled implements Node {
    private Task task;
    private Node previousNode;  // planning variable
    private Node nextNode;  // shadow
    private List<OperationAllocation> operationAllocations;


    public SequenceNode(String id, Task task, Node previousNode, List<OperationAllocation> operationAllocations) {
        super(id);
        this.task = task;
        this.previousNode = previousNode;
        this.operationAllocations = operationAllocations;
    }

    public SequenceNode(String id) {
        super(id);
    }

    public SequenceNode() {
        super(null);
    }

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED)
    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }


    public List<OperationAllocation> getOperationAllocations() {
        return operationAllocations;
    }

    public void setOperationAllocations(List<OperationAllocation> operationAllocations) {
        this.operationAllocations = operationAllocations;
    }

    public LocalDateTime getNodeEndDate(){
        return this.getOperationAllocations().getLast().getEndDate();
    }

    @ShadowVariable(variableListenerClass = NextNodeListener.class,sourceVariableName = "previousNode")
    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}
