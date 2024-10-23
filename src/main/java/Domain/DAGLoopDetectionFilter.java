package Domain;

import Domain.Allocation.Allocation;
import Domain.Allocation.ResourceNode;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import ai.timefold.solver.core.impl.heuristic.move.Move;
import ai.timefold.solver.core.impl.heuristic.selector.common.decorator.SelectionFilter;

import java.util.*;

public class DAGLoopDetectionFilter implements SelectionFilter<Scheduler, Move> {
    @Override
    public boolean accept(ScoreDirector<Scheduler> scoreDirector, Move move) {
        List<Allocation> allocationList = scoreDirector.getWorkingSolution().getAllocations();
        List<ResourceNode> resourceNodeList = scoreDirector.getWorkingSolution().getResourceNodes();
        int[][] adjMatrix = new int[allocationList.size()][allocationList.size()];

        // From craft path
        for (Allocation allocation1 : allocationList) {
            if (allocation1.getPredecessorsAllocations() != null) {
                for (Allocation allocation2 : allocation1.getPredecessorsAllocations()) {
                    adjMatrix[allocationList.indexOf(allocation2)][allocationList.indexOf(allocation1)] = 1;
                }
            }
            if (allocation1.getSuccessorsAllocations() != null) {
                for (Allocation allocation2 : allocation1.getSuccessorsAllocations())
                    adjMatrix[allocationList.indexOf(allocation1)][allocationList.indexOf(allocation2)] = 1;
            }
        }

        // From resource chained
        for (int i = 0; i < resourceNodeList.size(); i++) {
            if (resourceNodeList.get(i).getNext() != null) {
                Allocation cursor = resourceNodeList.get(i).getNext();
                while (cursor != null) {
                    adjMatrix[allocationList.indexOf(cursor)][allocationList.indexOf(cursor.getNext())] = 1;
                    cursor = cursor.getNext();
                }
            }
        }

        // DAG loop detection
        Set<Allocation> visited = new HashSet<>();
        ArrayDeque<Allocation> deque = new ArrayDeque<>();
        for (int i = 0; i < allocationList.size(); i++) {
            int degree = 0;
            for (int j = 0; j < allocationList.size(); j++) {
                if (adjMatrix[j][i] == 1) {
                    degree++;
                }
            }
            if (degree == 0) {
                deque.addFirst(allocationList.get(i));
            }
        }

        while (!deque.isEmpty()) {
            Allocation allocation = deque.removeFirst();
            boolean allocationIsReady = true;
            if (allocation.getPredecessorsAllocations() != null) {
                for (Allocation preAllocation : allocation.getPredecessorsAllocations()) {
                    if (!visited.contains(preAllocation)) {
                        allocationIsReady = false;
                        break;
                    }
                }
            }
            if (allocationIsReady) {
                visited.add(allocation);
                for (int i = 0; i < allocationList.size(); i++) {
                    adjMatrix[allocationList.indexOf(allocation)][i] = 0;
                }
                // Research 0 degree allocation
                for (int i = 0; i < allocationList.size(); i++) {
                    int degree = 0;
                    for (int j = 0; j < allocationList.size(); j++) {
                        if (adjMatrix[j][i] == 1) {
                            degree++;
                        }
                    }
                    if (degree == 0 && !visited.contains(allocationList.get(i))) {
                        deque.addFirst(allocationList.get(i));
                    }
                }

            } else {
                deque.addLast(allocation);
            }
        }

        if (visited.size() < allocationList.size()) {
            return false;
        } else if (visited.size() > allocationList.size()) {
            throw new RuntimeException("Visited size is larger than allocationList size");
        } else {
            return true;
        }
    }
}
