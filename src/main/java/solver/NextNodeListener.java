package solver;

import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import domain.Head;
import domain.SequenceNode;
import domain.Solution;
import domain.Tail;

import java.util.ArrayList;
import java.util.List;

public class NextNodeListener implements VariableListener<Solution, SequenceNode> {
    @Override
    public void beforeVariableChanged(ScoreDirector<Solution> scoreDirector, SequenceNode sequenceNode) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<Solution> scoreDirector, SequenceNode sequenceNode) {
        this.updateNextNode(scoreDirector, sequenceNode);
    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Solution> scoreDirector, SequenceNode sequenceNode) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<Solution> scoreDirector, SequenceNode sequenceNode) {
        this.updateNextNode(scoreDirector, sequenceNode);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Solution> scoreDirector, SequenceNode sequenceNode) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Solution> scoreDirector, SequenceNode sequenceNode) {

    }

    public void updateNextNode(ScoreDirector<Solution> scoreDirector, SequenceNode sequenceNode) {
        Solution solution = scoreDirector.getWorkingSolution();
        Head head = solution.getHead();
        Tail tail = solution.getTail();
        List<SequenceNode> sequenceNodes = solution.getSequenceNodeList();
        List<SequenceNode> sortedNodes = new ArrayList<>();

        // 找到第一个task
        for (SequenceNode node : sequenceNodes) {
            if (node.getPreviousNode() == head) {
                sortedNodes.add(node);
                break;
            }
        }

        while (sortedNodes.size() < sequenceNodes.size()) {
            for (SequenceNode node : sequenceNodes)
                if (node.getPreviousNode() == sortedNodes.getLast()) {
                    sortedNodes.add(node);
                }
        }

        // 反向遍历，更新next node
        for (int i = sortedNodes.size() - 1; i > 1; i--) {  // 不包含head
            if (i == sortedNodes.size() - 1) {
                scoreDirector.beforeVariableChanged(sequenceNode, "nextNode");
                sortedNodes.get(i).setNextNode(tail);
                scoreDirector.afterVariableChanged(sequenceNode, "nextNode");
            } else {
                scoreDirector.beforeVariableChanged(sequenceNode, "nextNode");
                sortedNodes.get(i).setNextNode(sortedNodes.get(i - 1));
                scoreDirector.afterVariableChanged(sequenceNode, "nextNode");

            }

        }

    }
}
