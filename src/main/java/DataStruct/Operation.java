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
    private boolean isFrozen;
    private Object frozenPrevious;  // ResourceNode or Operation

    // To judge the position of operation
    @NotNull
    private boolean isFirst;
    @NotNull
    private boolean isLast;



    public Operation() {
        super(null);
    }

    protected boolean OperationDataCheck() {
        Set<String> resourceIDs = new HashSet<>();
        for (ExecutionMode executionMode : this.getExecutionModes()) {
            if (executionMode.getResourceRequirement().getResourceNode().getTimeSlots().isEmpty()) {
                return false;
            }
            resourceIDs.add(executionMode.getResourceRequirement().getResourceNode().getId());
        }
        return resourceIDs.size() == this.getExecutionModes().size();
    }
}
