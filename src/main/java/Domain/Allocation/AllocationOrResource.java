package Domain.Allocation;

import Domain.AbstractPerishable;
import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@PlanningEntity
public abstract class AllocationOrResource extends AbstractPerishable {
    @InverseRelationShadowVariable(sourceVariableName = "previous")
    private Allocation next;

    public AllocationOrResource() {
    }

}
