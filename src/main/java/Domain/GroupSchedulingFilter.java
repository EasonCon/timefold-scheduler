package Domain;

import Domain.Allocation.Allocation;
import ai.timefold.solver.core.api.domain.entity.PinningFilter;

public class GroupSchedulingFilter implements PinningFilter<Scheduler, Allocation> {
    @Override
    public boolean accept(Scheduler scheduler, Allocation allocation) {
        // true == pinned
        return allocation.getOperation().getParentTask().getGroupSchedulingIndicator() != null;
    }
}
