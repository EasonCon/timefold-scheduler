package solver;

import ai.timefold.solver.core.api.score.buildin.simple.SimpleScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import domain.SequenceNode;

import java.time.Duration;

public class TimeConstrainsProvider implements ConstraintProvider {
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                this.minimizeLateCompletion(constraintFactory)
        };
    }

    public Constraint minimizeLateCompletion(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(SequenceNode.class)
                .filter(sequenceNode -> sequenceNode.getNodeEndDate().isAfter(sequenceNode.getTask().getDueDate()))
                .penalize(SimpleScore.ONE, sequenceNode ->
                        Duration.between(sequenceNode.getTask().getDueDate(), sequenceNode.getNodeEndDate()).toHoursPart()
                )
                .asConstraint("Late Completion Penalty");
    }
}
