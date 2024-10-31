package Domain.Listen;

import DataStruct.OperationStartRelationShip;
import Domain.Allocation.Allocation;
import Domain.Allocation.AllocationOrResource;
import DataStruct.ResourceNode;
import Domain.Scheduler;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;

import java.util.*;


/*
 * Markov Process: Ensure the previous and processor have the right start time.From current allocation, transfer the affect to the next and successor.
 * 1.Recalculate the start time by the previous allocation, execution mode and delay.
 * 2.Transfer the affect to the next allocation.
 * 3.EntityAdd and variableChange needs different way to handle.
 */

public class StartTimeListener implements VariableListener<Scheduler, Allocation> {
    @Override
    public void beforeVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
        updateStartTimeWhileMoving(scoreDirector, allocation);
    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    public void updateOneAllocationStartTime(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
        // Before all allocations get its position,all pointers of previous are null.
        if (allocation.getPrevious() == null) {
            return;
        }
//        if(allocation.getId() == "allocation1"){
//            System.out.println("allocation1");
//        }
        Long startTimeFromResource = allocation.TimeConstraintFromResource();
        Long startTimeFromPredecessors = allocation.TimeConstraintFromCraftPath();

        scoreDirector.beforeVariableChanged(allocation, "startTime");
        if (startTimeFromResource == null || startTimeFromPredecessors == null) {
            allocation.setStartTime(null);
        } else {
            allocation.setStartTime(Math.max(startTimeFromResource, startTimeFromPredecessors));
        }
        scoreDirector.afterVariableChanged(allocation, "startTime");

    }

    public void updateStartTimeWhileMoving(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
        updateOneAllocationStartTime(scoreDirector, allocation);
        List<Allocation> allocationList = scoreDirector.getWorkingSolution().getAllocations();
        List<ResourceNode> resourceNodeList = scoreDirector.getWorkingSolution().getResourceNodes();
        int[][] adjMatrix = new int[allocationList.size()][allocationList.size()];

        // Build adjMatrix
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

        for (int i = 0; i < resourceNodeList.size(); i++) {
            if (resourceNodeList.get(i).getNext() != null) {
                Allocation cursor = resourceNodeList.get(i).getNext();
                while (cursor.getNext() != null) {
                    adjMatrix[allocationList.indexOf(cursor)][allocationList.indexOf(cursor.getNext())] = 1;
                    cursor = cursor.getNext();
                }
            }
        }

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
            Allocation head = deque.removeFirst();
            boolean allocationIsReady = true;
            if (head.getPredecessorsAllocations() != null) {
                for (Allocation preAllocation : head.getPredecessorsAllocations()) {
                    if (!visited.contains(preAllocation)) {
                        allocationIsReady = false;
                        break;
                    }
                }
            }
            if (allocationIsReady) {
                //
                if (head.getStartTime() == null || allocation.getStartTime() == null) {
                    continue;
                }
                if (head.getStartTime() > allocation.getStartTime()) {
                    this.updateOneAllocationStartTime(scoreDirector, head);
                }
                visited.add(head);
                for (int i = 0; i < allocationList.size(); i++) {
                    adjMatrix[allocationList.indexOf(head)][i] = 0;
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
                deque.addLast(head);
            }
        }
    }

    protected void notifyWorkingSolutionChanged(Allocation allocation) {
    }
}
