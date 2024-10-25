package Domain.Listen;

import DataStruct.ExecutionMode;
import Domain.Allocation.Allocation;
import Domain.Allocation.AllocationOrResource;
import Domain.Allocation.ResourceNode;
import Domain.Scheduler;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;

import java.util.ArrayList;
import java.util.List;

public class PreviousAllocationRangeListener implements VariableListener<Scheduler, Allocation> {
    @Override
    public void beforeVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
        updatePreviousAllocationRange(scoreDirector, allocation);
    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
        updatePreviousAllocationRange(scoreDirector, allocation);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    public void updatePreviousAllocationRange(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
        // 1.Resource from operation execution mode.
        // 2.Allocation from resource chained.
        List<ResourceNode> resourceList = new ArrayList<>();
        for (ExecutionMode executionMode : allocation.getOperation().getExecutionModes()) {
            resourceList.add(executionMode.getResourceRequirement().getResourceNode());
        }

        List<AllocationOrResource> allocations = new ArrayList<>(resourceList);
        for (ResourceNode resourceNode : resourceList) {
            if (resourceNode.getNext() != null) {
                Allocation cursor = resourceNode.getNext();
                while (cursor != null) {
                    if (cursor != allocation) {
                        allocations.add(cursor);
                    }
                    cursor = cursor.getNext();
                }
            }
        }

        scoreDirector.beforeVariableChanged(allocation, "possiblePreviousAllocation");
        allocation.setPossiblePreviousAllocation(allocations);
        scoreDirector.afterVariableChanged(allocation, "possiblePreviousAllocation");

    }
}
