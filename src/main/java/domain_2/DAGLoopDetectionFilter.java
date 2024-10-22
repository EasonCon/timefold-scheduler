package domain_2;

import DataStruct.Resource.RenewableResource;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import ai.timefold.solver.core.impl.heuristic.move.Move;
import ai.timefold.solver.core.impl.heuristic.selector.common.decorator.SelectionFilter;

import java.util.*;

public class DAGLoopDetectionFilter implements SelectionFilter<Solver, Move> {


    @Override
    public boolean accept(ScoreDirector<Solver> scoreDirector, Move move) {
        // From result of move to judge. Move.getPlanningEntities() returns a collection.
        List<Node> nodes = scoreDirector.getWorkingSolution().getNodes();
        List<RenewableResource> resourceList = scoreDirector.getWorkingSolution().getResources();

        // DAG loop detection
        Map<String, List<Node>> resourceNodeMap = new HashMap<>();
        for (RenewableResource resource : resourceList) {
            resourceNodeMap.put(resource.getId(), new ArrayList<>());
        }

        for (Node node : nodes) {
            resourceNodeMap.get(node.getExecutionMode().getResourceRequirement().getRenewableResource().getId()).add(node);
        }

        // Constructing the adjacency matrix
        int[][] adjMatrix = new int[nodes.size()][nodes.size()];
        for (List<Node> value : resourceNodeMap.values()) {
            for (int i = 0; i < value.size() - 1; i++)
                adjMatrix[nodes.indexOf(value.get(i))][nodes.indexOf(value.get(i + 1))] = 1;
        }
        for (Node node : nodes) {
            if (node.getSuccessorsNodes() != null) {
                for (Node next : node.getSuccessorsNodes()) {
                    adjMatrix[nodes.indexOf(node)][nodes.indexOf(next)] = 1;
                }
            }
        }

        Set<Node> visited = new HashSet<>();
        ArrayDeque<Node> deque = new ArrayDeque<>();
        for (int i = 0; i < nodes.size(); i++) { // col
            int degree = 0;
            for (int j = 0; j < nodes.size(); j++) {   // row
                if (adjMatrix[j][i] == 1) {
                    degree += 1;
                }
            }
            if (degree == 0) {
                deque.add(nodes.get(i));
            }
        }

        while (!deque.isEmpty()) {
            Node node = deque.removeFirst();
            boolean nodeIsReady = true;
            if (node.getProcessorsNodes() != null) {
                for (Node preNode : node.getProcessorsNodes()) {
                    if (!visited.contains(preNode)) {
                        nodeIsReady = false;
                        break;
                    }
                }
            }
            if (node.getOperation().isFirst() && nodeIsReady) {
                visited.add(node);
                // update adjMatrix
                for (int i = 0; i < nodes.size(); i++) {
                    adjMatrix[nodes.indexOf(node)][i] = 0;
                }
                // Re-search 0 degree nodes
                for (int i = 0; i < nodes.size(); i++) { // col
                    int degree = 0;
                    for (int j = 0; j < nodes.size(); j++) { // row
                        if (adjMatrix[j][i] == 1) {
                            degree += 1;
                        }
                    }
                    if (degree == 0 && !deque.contains(nodes.get(i))) {
                        deque.addLast(nodes.get(i));
                    }
                }
            } else {
                deque.addLast(node);  // Node cannot be executed, put it back to the deque
            }
        }

        if (visited.size() < nodes.size()) {
            return false;
        } else if (visited.size() > nodes.size()) {
            throw new RuntimeException("Visited size is larger than nodes size");
        } else {
            return true;
        }
    }
}




