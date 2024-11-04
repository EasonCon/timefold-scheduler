package DataStruct;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import jakarta.validation.constraints.NotNull;


@Getter
@Setter
public class Task extends Labeled {
    private String materialId;
    private String materialDescription;
    @NotNull(message = "Production quantity cannot be null")
    private float quantity;

    @NotNull(message = "Craft path cannot be null")
    private List<Operation> craftPath = new ArrayList<>();
    private List<MasterDemand> masterDemands = new ArrayList<>();
    private SupplyType supplyType;
    private Integer priority;
    private Long earliestStartTime;
    private Long latestStartTime;
    private Long materialSetTime;


    public Task() {
        super(null);
    }

    protected boolean TaskDataCheck() {
        /*
         * 1. Ring detection
         * 2. Operation resource check
         */
        List<Operation> firstOperation = new ArrayList<>();
        for (Operation operation : this.getCraftPath()) {
            if (operation.isFirst()) {
                firstOperation.add(operation);
            }
        }
        if (firstOperation.isEmpty()) {
            return false;
        }
        Deque<Operation> deque = new ArrayDeque<>(firstOperation);
        Set<Operation> visited = new HashSet<>();
        while (!deque.isEmpty()) {
            Operation operation = deque.removeFirst();
            if (visited.contains(operation)) {
                return false;
            }

            // Operation resource check
            if (!operation.OperationDataCheck()) {
                return false;
            }

            visited.add(operation);
            for (Operation nextOperation : operation.getNextOperations()) {
                deque.addLast(nextOperation);
            }
        }
        return true;
    }
}
