package Domain;

import Domain.Allocation.Allocation;
import DataStruct.ResourceNode;
import Domain.Allocation.AllocationOrResource;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import ai.timefold.solver.core.impl.heuristic.move.Move;

import ai.timefold.solver.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import ai.timefold.solver.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class DAGLoopDetectionFilter implements SelectionFilter<Scheduler, ChangeMove> {
    private static final Logger logger = LoggerFactory.getLogger(DAGLoopDetectionFilter.class);

    @Override
    public boolean accept(ScoreDirector<Scheduler> scoreDirector, ChangeMove move) {
        // TODO:O(n^2) --> O(V·E)
        // TODO:Store degree of each node
        logger.info("Start Loop Detection Filter");

        // debug
        System.out.println("当前链路情况:");
        for (ResourceNode resourceNode : scoreDirector.getWorkingSolution().getResourceNodes()) {
            System.out.print(resourceNode.getId());
            if (resourceNode.getNext() != null) {
                Allocation allocation = resourceNode.getNext();
                while (allocation != null) {
                    System.out.print(" -> " + allocation.getId() + ":" + allocation.getStartTime());
                    allocation = allocation.getNext();
                }
            }
            System.out.println();
        }
        System.out.println("当前Move:");
        System.out.println(move.toString());
        // debug

        Allocation currentAllocation = (Allocation) move.getEntity();
        AllocationOrResource targetPrevious = (AllocationOrResource) move.getToPlanningValue();
        if (Objects.equals(currentAllocation.getPrevious().getId(), targetPrevious.getId())) {
            return false;
        }
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
                while (cursor.getNext() != null) {
                    adjMatrix[allocationList.indexOf(cursor)][allocationList.indexOf(cursor.getNext())] = 1;
                    cursor = cursor.getNext();
                }
            }
        }

        // Modify adjMatrix: Because current solution is not changed.
        // ** Deal with current allocation
        int currentAllocationIndex = allocationList.indexOf(currentAllocation);
        if (currentAllocation.getNext() != null) {
            int nextAllocationIndex = allocationList.indexOf(currentAllocation.getNext());
            this.setAdjMatrix(currentAllocation,currentAllocation.getNext(),adjMatrix,allocationList);
            if (currentAllocation.getPrevious() instanceof Allocation allocationPrevious) {
                int previousAllocationIndex = allocationList.indexOf(allocationPrevious);
//                adjMatrix[previousAllocationIndex][currentAllocationIndex] = 0;
                this.setAdjMatrix(allocationPrevious,currentAllocation,adjMatrix,allocationList);
                adjMatrix[previousAllocationIndex][nextAllocationIndex] = 1;
            }
        }

        // ** Deal with target allocation
        if (targetPrevious instanceof Allocation) {
            int targetAllocationIndex = allocationList.indexOf(targetPrevious);
            adjMatrix[targetAllocationIndex][currentAllocationIndex] = 1;
            if (targetPrevious.getNext() != null) {
                int nextAllocationIndex = allocationList.indexOf(targetPrevious.getNext());
//                adjMatrix[targetAllocationIndex][nextAllocationIndex] = 0;
                this.setAdjMatrix((Allocation) targetPrevious,targetPrevious.getNext(),adjMatrix,allocationList);
                adjMatrix[currentAllocationIndex][nextAllocationIndex] = 1;
            }
        } else {
            if (targetPrevious.getNext() != null) {
                int nextAllocationIndex = allocationList.indexOf(targetPrevious.getNext());
                adjMatrix[currentAllocationIndex][nextAllocationIndex] = 1;
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
        }
        if (visited.size() < allocationList.size()) {
            logger.info("Loop detected in DAG");
            return false;
        } else if (visited.size() > allocationList.size()) {
            throw new RuntimeException("Visited size is larger than allocationList size");
        } else {
            logger.info("DAGLoopDetectionFilter accept");
            return true;
        }
    }

    protected void setAdjMatrix(Allocation previous, Allocation next, int [][] adjMatrix,List<Allocation> allocationList) {
        for(Allocation allocation:previous.getSuccessorsAllocations()){
            if(Objects.equals(allocation.getId(), next.getId())){
                return;
            }
        }
        adjMatrix[allocationList.indexOf(previous)][allocationList.indexOf(next)] = 0;
    }

}
