package Domain.Allocation;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@PlanningEntity
public abstract class AllocationOrResource {
    @InverseRelationShadowVariable(sourceVariableName = "previous")
    private Allocation next;

    public AllocationOrResource() {
    }

}
