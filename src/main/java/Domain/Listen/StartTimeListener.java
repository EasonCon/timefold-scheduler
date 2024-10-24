package Domain.Listen;

import Domain.Allocation.Allocation;
import Domain.Allocation.ResourceNode;
import Domain.Scheduler;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;


/*
 * Markov Process: Ensure the previous and processor have the right start time.From current allocation, transfer the affect to the next and successor.
 * 1.Recalculate the start time by the previous allocation, execution mode and delay.
 * 2.Transfer the affect to the next allocation.
 */

public class StartTimeListener implements VariableListener<Scheduler, Allocation> {
    @Override
    public void beforeVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {

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

    public void updateStartTime(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
        if (allocation.getPrevious() instanceof ResourceNode && allocation.getPredecessorsAllocations() == null) {
            scoreDirector.beforeVariableChanged(allocation, "startTime");
            allocation.setStartTime(allocation.getResourceNode().getTimeSlots().getFirst().getStart());
            scoreDirector.afterVariableChanged(allocation, "startTime");
        } else {
            Allocation previousAllocation = (Allocation) allocation.getPrevious();
        }


        // Alternating transmission of influence
        // 思路：从当前allocation开始向后构建DAG，利用环检测方法更新start time TODO
        scoreDirector.beforeVariableChanged(allocation, "startTime");
        allocation.setStartTime(3L);
        scoreDirector.afterVariableChanged(allocation, "startTime");

    }
}
