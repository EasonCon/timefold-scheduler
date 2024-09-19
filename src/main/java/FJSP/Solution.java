package FJSP;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.simple.SimpleScore;
import base_domain.Labeled;
import base_domain.Operation;
import base_domain.Resource;
import base_domain.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@PlanningSolution
public class Solution extends Labeled {
    private List<Task> taskList;
    private List<Resource> resourceList;
    private SinkAllocation sinkAllocation;
    private SourceAllocation sourceAllocation;
    private List<OperationAllocation> operationAllocationList;  // value range
    private HashMap<Task, List<OperationAllocation>> operationAllocationMap;
    private SimpleScore score;

    public Solution() {
        super(null);
    }

    @ProblemFactCollectionProperty
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public SinkAllocation getSinkAllocation() {
        return sinkAllocation;
    }

    public void setSinkAllocation(SinkAllocation sinkAllocation) {
        this.sinkAllocation = sinkAllocation;
    }

    @ProblemFactCollectionProperty
    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    public SourceAllocation getSourceAllocation() {
        return sourceAllocation;
    }

    public void setSourceAllocation(SourceAllocation sourceAllocation) {
        this.sourceAllocation = sourceAllocation;
    }

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "operationAllocationRange")
    public List<OperationAllocation> getOperationAllocationList() {
        return operationAllocationList;
    }

    public void setOperationAllocationList(List<OperationAllocation> operationAllocationList) {
        this.operationAllocationList = operationAllocationList;
    }

    public HashMap<Task, List<OperationAllocation>> getOperationAllocationMap() {
        return operationAllocationMap;
    }

    public void setOperationAllocationMap(HashMap<Task, List<OperationAllocation>> operationAllocationMap) {
        this.operationAllocationMap = operationAllocationMap;
    }

    // ************************************************************************
    // Complex methods: FCFS时间推导，在变量监听结束后调用更新时间TODO 非增量式：chained模式可能做到增量式？
    // ************************************************************************

    public void calculateTimes() {
        this.getOSSequence();  // 按照os编码编排allocation
        this.clearResource();  // 清空资源时间
        HashMap<Task,Integer> taskMap = new HashMap<>();  // 计算字典
        for (OperationAllocation operationAllocation : this.operationAllocationList) {
            taskMap.put(operationAllocation.getOperation().getParentTask(),0);
        }

        for (OperationAllocation operationAllocation : this.operationAllocationList) {
            Resource resource = operationAllocation.getAssignedExecutionMode().getResource();
            Operation operation = operationAllocation.getOperation();

            // 计算开始时间
            if(this.operationAllocationMap.get(operationAllocation.getOperation().getParentTask()).indexOf(operationAllocation) == 0) {
                operationAllocation.setStartTime(resource.getValidStartTime(resource.getLocalDateTime()));  // 判定时间合理
            }
            else{
                LocalDateTime startTime = resource.getLocalDateTime();
                LocalDateTime lastOperationEnd = this.operationAllocationMap.get(operation.getParentTask()).get(taskMap.get(operation.getParentTask()) - 1).getEndTime();
                if(lastOperationEnd.isAfter(startTime)){
                    startTime = lastOperationEnd;
                }
                operationAllocation.setStartTime(startTime);
            }

            // 计算结束时间
            LocalDateTime endTime;
            Duration duration = Duration.ofMinutes((long)(operation.getParentTask().getUnclearedQuantity() * operation.getMinutesPerBeat() / (operation.getQuantityPerBeat() + 1)));
//            endTime = resource.getValidEndTime(operationAllocation.getStartTime(),duration);  // TODO
            endTime = operationAllocation.getStartTime().plus(duration);
            operationAllocation.setEndTime(endTime);
            resource.setLocalDateTime(endTime);
            taskMap.put(operation.getParentTask(),taskMap.get(operation.getParentTask()) + 1);  // 更新计数器
        }
    }

    public void calculateMakespan() {
        this.getOSSequence();
        for(OperationAllocation operationAllocation : this.operationAllocationList){
            operationAllocation.setStartTime(operationAllocation.getAssignedExecutionMode().getResource().getLocalDateTime());
            operationAllocation.setEndTime(operationAllocation.getAssignedExecutionMode().getResource().getLocalDateTime());
        }
    }

    protected void getOSSequence() {
        // os编码:重排allocation
        List<OperationAllocation> osSequence = new ArrayList<>();
        HashMap<Task, Integer> taskOSMap = new LinkedHashMap<>();  // 任务计数器
        for (Task task : this.taskList) {
            taskOSMap.put(task, 0);
        }
        for (OperationAllocation operationAllocation : this.operationAllocationList) {
            Task task = operationAllocation.getOperation().getParentTask();
            osSequence.add(this.operationAllocationMap.get(task).get(taskOSMap.get(task)));
            taskOSMap.put(task, taskOSMap.get(task) + 1);
        }
        System.out.println(this.operationAllocationList.equals(osSequence));
        this.operationAllocationList = osSequence;


        // 保持结构
        this.operationAllocationList.getFirst().setPreviousOperationAllocation(sourceAllocation);
        this.operationAllocationList.getFirst().setNextOperationAllocation(this.operationAllocationList.get(1));
        this.operationAllocationList.getLast().setNextOperationAllocation(sinkAllocation);
        this.operationAllocationList.getLast().setPreviousOperationAllocation(this.operationAllocationList.get(this.operationAllocationList.size() - 2));

        for (int i = 1; i < this.operationAllocationList.size() - 1; i++) {
            this.operationAllocationList.get(i).setPreviousOperationAllocation(this.operationAllocationList.get(i - 1));
            this.operationAllocationList.get(i).setNextOperationAllocation(this.operationAllocationList.get(i + 1));
        }

    }

    protected void clearResource(){
        for (Resource resource : this.resourceList){
            resource.setLocalDateTime(resource.getTimeSlots().getFirst().getStart());
        }
    }

    @PlanningScore
    public SimpleScore getScore() {
        return score;
    }

    public void setScore(SimpleScore score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
