package solver;

import FJSP.OperationAllocation;
import FJSP.Solution;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;

import java.util.List;

public class OperationAllocationVariableListener implements VariableListener<Solution, OperationAllocation> {


    @Override
    public void beforeVariableChanged(ScoreDirector<Solution> scoreDirector, OperationAllocation operationAllocation) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<Solution> scoreDirector, OperationAllocation operationAllocation) {
        updateShadow(scoreDirector, operationAllocation);
    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Solution> scoreDirector, OperationAllocation operationAllocation) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<Solution> scoreDirector, OperationAllocation operationAllocation) {
//        updateShadow(scoreDirector, operationAllocation);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Solution> scoreDirector, OperationAllocation operationAllocation) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Solution> scoreDirector, OperationAllocation operationAllocation) {

    }

    public void updateShadow(ScoreDirector<Solution> scoreDirector, OperationAllocation operationAllocation){
        Solution solution = scoreDirector.getWorkingSolution();
        solution.calculateTimes();


    }
}
