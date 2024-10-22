package Domain;

import Domain.Allocation.Allocation;
import Domain.Allocation.AllocationOrResource;
import Domain.Allocation.ResourceNode;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import ai.timefold.solver.core.impl.heuristic.move.AbstractMove;
import ai.timefold.solver.core.impl.heuristic.move.Move;

public class AnchorChangedMove extends AbstractMove<Scheduler> {
    private ResourceNode toResourceNode;
    private Allocation allocation;

    @Override
    protected Move createUndoMove(ScoreDirector scoreDirector) {
        return null;
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector scoreDirector) {
        scoreDirector.beforeVariableChanged(allocation, "previous");
        allocation.setPrevious(toResourceNode);  // 同时触发监听
        scoreDirector.afterVariableChanged(allocation, "previous");
    }

    @Override
    public boolean isMoveDoable(ScoreDirector scoreDirector) {
        return allocation.getResourceNode() != toResourceNode;
    }
}
