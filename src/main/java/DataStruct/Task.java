package DataStruct;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Getter
@Setter
public class Task extends Labeled {
    private static final Logger logger = LoggerFactory.getLogger(Task.class);
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
    private String groupSchedulingIndicator;


    public Task() {
        super(null);
    }

    public boolean TaskDataCheck(List<ResourceNode> allResources) {
        /*
         * 1. Ring detection
         * 2. Operation resource check
         */
        List<Operation> firstOperation = new ArrayList<>();
        for (Operation operation : this.getCraftPath()) {
            if (operation.isFirst()) {
                firstOperation.add(operation);
            }
            if (!operation.OperationDataCheck(allResources)) {
                logger.warn("Task {} has an operation {} with invalid data", this.getId(), operation.getId());
                return false;
            }
            if (operation.getFrozenPrevious() instanceof ResourceNode resourceNode && !allResources.contains(resourceNode)) {
                logger.warn("Task {} has an frozen operation {},which planned resource is not available", this.getId(), operation.getId());
                return false;
            }
        }
        if (firstOperation.isEmpty()) {
            logger.warn("Task {} has no first operation", this.getId());
            return false;
        }
        return true;

//        // check if loop exist
//        Deque<Operation> deque = new ArrayDeque<>(firstOperation);
//        Set<Operation> visited = new HashSet<>();
//        while (!deque.isEmpty()) {
//            Operation operation = deque.removeFirst();
//            if (visited.contains(operation)) {
//                logger.warn("Task {} has a ring", this.getId());
//                return false;
//            }
//
//            // Operation resource check
//            if (!operation.OperationDataCheck(allResources)) {
//                logger.warn("Task {} has an operation {} with invalid data", this.getId(), operation.getId());
//                return false;
//            }
//
//            visited.add(operation);
//            for (Operation nextOperation : operation.getNextOperations()) {
//                deque.addLast(nextOperation);
//            }
//        }
//        return true;
    }
}
