package domain_2;

import DataStruct.Operation;
import DataStruct.Resource.RenewableResource;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;

import java.util.*;

public class NodeStartTimeListener implements VariableListener<Solver, Node> {
    // If start time trigger works,it must pass loop detection.

    @Override
    public void beforeVariableChanged(ScoreDirector<Solver> scoreDirector, Node node) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<Solver> scoreDirector, Node node) {
        updateEntityStartTime(scoreDirector, node);
    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Solver> scoreDirector, Node node) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<Solver> scoreDirector, Node node) {
        updateEntityStartTime(scoreDirector, node);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Solver> scoreDirector, Node node) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Solver> scoreDirector, Node node) {

    }

    public void updateEntityStartTime(ScoreDirector<Solver> scoreDirector, Node node) {
        List<Node> preNodes = new ArrayList<>();
        RenewableResource thisNodeResource = node.getExecutionMode().getResourceRequirement().getRenewableResource();

        Node cursor = node;
        while (cursor.getPreviousNode() != null) {
            preNodes.add(cursor.getPreviousNode());
            cursor = cursor.getPreviousNode();
        }

        preNodes.sort(Comparator.comparing(Node::getStartTime)); // Sort by start time

        Map<RenewableResource, List<Node>> resourceNodeMap = new HashMap<>();  // To record the last node of each resource

        for (Node preNode : preNodes) {
            RenewableResource resource = preNode.getExecutionMode().getResourceRequirement().getRenewableResource();
            resourceNodeMap.computeIfAbsent(resource, k -> new ArrayList<>()).add(preNode);
        }


        // TODO这里可能存在的问题:简单按照start time对资源上的job排序可能是出现一些暂时不可预知的问题，尤其是属于同一个task的job时
        // TODO:边界处理
        while (node.getNextNode() != null) {
            // Calculate the start time constraint from craft path
            long processorEndTime = 0;
            for (Node processor : node.getProcessorsNodes()) {
                if (processor.getEndTime() > processorEndTime) {
                    processorEndTime = processor.getEndTime();
                }

                Long startTime = Math.max(
                        resourceNodeMap.getOrDefault(thisNodeResource, new LinkedList<>()).isEmpty()
                                ? processorEndTime
                                : resourceNodeMap.get(thisNodeResource).getLast().getEndTime(),
                        processorEndTime
                );
                if (startTime.equals(node.getStartTime())) {
                    return;
                }
                scoreDirector.beforeVariableChanged(node, "startTime");
                node.setStartTime(startTime);
                scoreDirector.afterVariableChanged(node, "startTime");
                resourceNodeMap.computeIfAbsent(thisNodeResource, k -> new ArrayList<>()).add(node);
            }
        }

    }

}