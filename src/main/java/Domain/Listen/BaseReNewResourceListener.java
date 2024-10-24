package Domain.Listen;

import Domain.Allocation.Allocation;
import Domain.Allocation.AllocationOrResource;
import Domain.Allocation.ResourceNode;
import Domain.Scheduler;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;

public class BaseReNewResourceListener implements  VariableListener<Scheduler, Allocation>{

    @Override
    public void beforeVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//        updateBaseResource(scoreDirector,allocation);
    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//        updateBaseResource(scoreDirector,allocation);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

//    public void updateBaseResource(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//        ResourceNode newResource = allocation.getExecutionMode().getResourceRequirement().getResourceNode();
//        scoreDirector.beforeVariableChanged(allocation, "baseResource");
//        allocation.setBaseResource(newResource);
//        scoreDirector.afterVariableChanged(allocation, "baseResource");
//
//        // Delete from current list, inverse next update automatically
//        if (allocation.getNext() != null) {
//            scoreDirector.beforeVariableChanged(allocation.getNext(), "previous");
//            allocation.getNext().setPrevious(allocation.getPrevious());
//            scoreDirector.afterVariableChanged(allocation.getNext(), "previous");
//        }
//
//        // Add to new list TODO:Currently add at last,may cause loop problem
//        AllocationOrResource cursor = newResource;
//        while (cursor.getNext() != null) {
//            cursor = cursor.getNext();
//        }
//
//        // Try to trigger anchor variable listener
//        scoreDirector.beforeVariableChanged(allocation, "previous");
//        allocation.setPrevious(cursor);
//        scoreDirector.afterVariableChanged(allocation, "previous");
//
//        // scoreDirector.triggerVariableListeners();
//    }
}
