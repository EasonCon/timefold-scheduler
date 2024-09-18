package domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import base_domain.Labeled;
import base_domain.Operation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@PlanningEntity
public class OperationAllocation extends Labeled {
    private Operation operation;
    private ExecutionMode assignedExecutionMode; // planning variable
    private List<ExecutionMode> executionModes;  // value provider

    private SequenceNode node;
    private OperationAllocation previousOperationAllocation;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public OperationAllocation(String id, Operation operation, ExecutionMode assignedExecutionMode, List<ExecutionMode> executionModes) {
        super(id);
        this.operation = operation;
        this.assignedExecutionMode = assignedExecutionMode;
        this.executionModes = executionModes;
    }

    public OperationAllocation(String id) {
        super(id);
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @PlanningVariable
    public ExecutionMode getAssignedExecutionMode() {
        return assignedExecutionMode;
    }

    public void setAssignedExecutionMode(ExecutionMode assignedExecutionMode) {
        this.assignedExecutionMode = assignedExecutionMode;
    }

    @ValueRangeProvider
    public List<ExecutionMode> getExecutionModes() {
        return executionModes;
    }

    public void setExecutionModes(List<ExecutionMode> executionModes) {
        this.executionModes = executionModes;
    }

    public SequenceNode getNode() {
        return node;
    }

    public void setNode(SequenceNode node) {
        this.node = node;
    }

    public OperationAllocation getPreviousOperationAllocation() {
        return previousOperationAllocation;
    }

    public void setPreviousOperationAllocation(OperationAllocation previousOperationAllocation) {
        this.previousOperationAllocation = previousOperationAllocation;
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

    // ************************************************************************
    // Complex methods
    // ************************************************************************


    //    // 下面两个方法发生了 工序数量深度的递归
//    public LocalDateTime getStartDate() {
//        LocalDateTime possibleStart;
//        if (this.getPreviousOperationAllocation() == null) { // 第一个operation
//            possibleStart = this.getAssignedExecutionMode().getResource().getLocalDateTime();
//
//        } else {
//            LocalDateTime previousEndDate = this.getPreviousOperationAllocation().getEndDate();
//            LocalDateTime resourceStartDate = this.getAssignedExecutionMode().getResource().getLocalDateTime();
//            possibleStart = previousEndDate.isAfter(resourceStartDate) ? previousEndDate : resourceStartDate;
//        }
////        return this.getAssignedExecutionMode().getResource().getValidStartTime(possibleStart);
//        return this.getAssignedExecutionMode().getResource().getLocalDateTime();
//    }
//
//
//    // TODO:shadow
//    public LocalDateTime getEndDate() {
//        float duration = this.getOperation().getParentTask().getUnclearedQuantity()
//                * this.getOperation().getMinutesPerBeat() / (this.getOperation().getQuantityPerBeat() + 1);
//        long wholeMinutes = (long) duration;
//        long seconds = (long) (duration - wholeMinutes) * 60;
//        Duration timeDelta = Duration.ofMinutes(wholeMinutes).plusSeconds(seconds);
////        return this.getAssignedExecutionMode().getResource().getValidEndTime(this.getStartDate(), timeDelta);
//        return this.getAssignedExecutionMode().getResource().getLocalDateTime();
//    }
    public void calculateOperationTime() {
        SequenceNode node = this.getNode(); // 假设 getNode() 返回一个单个对象
        List<SequenceNode> sequenceNodes = new ArrayList<>();
        sequenceNodes.add(node);

        // 向后查询
        while (node.getNextNode() != null) {
            Node nextNode = node.getNextNode();
            if (nextNode instanceof SequenceNode) {
                sequenceNodes.add(node);
                node = (SequenceNode) nextNode;
            } else {
                break;
            }
        }

        // 向前查询
        while (node.getPreviousNode() != null) {
            Node previousNode = node.getPreviousNode();
            if (previousNode instanceof SequenceNode) {
                sequenceNodes.add(node);
                node = (SequenceNode) previousNode;
            } else
                break;
        }

        // 计算工序时间
        // 先设置local time

    }

}
