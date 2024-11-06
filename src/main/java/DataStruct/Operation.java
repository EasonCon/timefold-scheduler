package DataStruct;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class Operation extends Labeled {
    private String name;
    @NotNull(message = "Parent task can't be null")
    private Task parentTask;

    private Long batchSize;
    private Integer order;

    @NotNull(message = "Operation Quantity can't be null")
    private long quantity;

    private List<ExecutionMode> executionModes = new ArrayList<>();
    private List<Operation> previousOperations = new ArrayList<>();
    private List<Operation> nextOperations = new ArrayList<>();
    private ProductionMethod productionMethod;
    private int resourceOccupiedPostTime = 0;
    private int NonResourceOccupiedPostTime = 0;
    private int resourceOccupiedPreparation = 0;
    private int NonResourceOccupiedPreparation = 0;
    private OperationStartRelationShip operationStartRelationShip = OperationStartRelationShip.ES;
    private boolean isCriticalPath = true;

    // Scheduling result from last time
    @NotNull(message = "Operation IsLocked can't be null")
    private boolean isLocked;
    private Long plannedStartTime;
    private Long plannedEndTime;
    private ResourceNode plannedResource;

    @NotNull(message = "Operation IsFrozen can't be null")
    private boolean isFrozen;  // isFrozen and frozenPrevious must exist together
    private Object frozenPrevious;  // ResourceNode or Operation

    // To judge the position of operation
    @NotNull
    private boolean isFirst;
    @NotNull
    private boolean isLast;


    public Operation() {
        super(null);
    }

    protected boolean OperationDataCheck(List<ResourceNode> allResources) {
        // An error is returned only if the process cannot be produced
        Set<String> resourceIDs = new HashSet<>();
        List<ExecutionMode> toRemoveExecutionModes = new ArrayList<>();
        for (ExecutionMode executionMode : this.getExecutionModes()) {
            if (!allResources.contains(executionMode.getResourceRequirement().getResourceNode())) {
                toRemoveExecutionModes.add(executionMode);
            } else {
                resourceIDs.add(executionMode.getResourceRequirement().getResourceNode().getId());
            }
        }
        this.getExecutionModes().removeAll(toRemoveExecutionModes);
        return !this.getExecutionModes().isEmpty();
    }
}
