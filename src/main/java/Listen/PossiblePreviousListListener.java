package Listen;

import DataStruct.ExecutionMode;
import Domain.Allocation.Allocation;
import Domain.Scheduler;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;

public class PossiblePreviousListListener implements VariableListener<Scheduler, ExecutionMode> {
    @Override
    public void beforeVariableChanged(ScoreDirector<Scheduler> scoreDirector, ExecutionMode executionMode) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<Scheduler> scoreDirector, ExecutionMode executionMode) {

    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Scheduler> scoreDirector, ExecutionMode executionMode) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<Scheduler> scoreDirector, ExecutionMode executionMode) {

    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Scheduler> scoreDirector, ExecutionMode executionMode) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Scheduler> scoreDirector, ExecutionMode executionMode) {

    }
}
