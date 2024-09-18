package domain;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.simple.SimpleScore;
import base_domain.Resource;
import base_domain.Task;

import java.util.List;

@PlanningSolution
public class Solution {
    private List<Task> taskList;
    private List<SequenceNode> sequenceNodeList;
    private SimpleScore score;

    private Head head;
    private Tail tail;

    public Solution(List<SequenceNode> sequenceNodeList, List<Task> taskList, SimpleScore score) {
        this.sequenceNodeList = sequenceNodeList;
        this.taskList = taskList;
        this.score = score;
    }

    public Solution() {
    }

    @ProblemFactCollectionProperty
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

//    @ProblemFactCollectionProperty
//    public List<Resource> getResourceList() {
//        return resourceList;
//    }
//
//    public void setResourceList(List<Resource> resourceList) {
//        this.resourceList = resourceList;
//    }

    @PlanningEntityCollectionProperty
    @ValueRangeProvider
    public List<SequenceNode> getSequenceNodeList() {
        return sequenceNodeList;
    }

    public void setSequenceNodeList(List<SequenceNode> sequenceNodeList) {
        this.sequenceNodeList = sequenceNodeList;
    }

    @PlanningScore
    public SimpleScore getScore() {
        return score;
    }

    public void setScore(SimpleScore score) {
        this.score = score;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Tail getTail() {
        return tail;
    }

    public void setTail(Tail tail) {
        this.tail = tail;
    }
}
