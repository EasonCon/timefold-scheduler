package DataStruct;

import DataStruct.Resource.RenewableResource;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Operation extends Labeled {
    // multiple predecessors are allowed but only one successor, which means that the craft is a tree.
    private String name;
    private int order;
    private Task parentTask;
    private List<ExecutionMode> executionModes = new ArrayList<>();
    private List<Operation> previousOperations = new ArrayList<>();
    private List<Operation> nextOperations = new ArrayList<>();
    private ProductionMethod productionMethod;
    private long resourceOccupiedPostTime;
    private long NonResourceOccupiedPostTime;
    private OperationStartRelationShip operationStartRelationShip;

    // keep stable scheduling result
    private boolean isLocked;
    private int plannedStartTime;
    private int plannedEndTime;
    private RenewableResource plannedResource;

    // for judging position of operation
    private boolean isFirst;
    private boolean isLast;


    public Operation() {
        super(null);
    }
}
