package domain_2;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@PlanningEntity
public class Head {
    @PlanningId
    private String id;
    @PlanningListVariable  // Move of planning list variable: changed move/swap move/sublist(change swap mov) / pillar move
    private List<Node> nodes = new ArrayList<>();

    public Head() {
    }
}
