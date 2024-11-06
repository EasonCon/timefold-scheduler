package App;

import DataStruct.*;
import Domain.Allocation.Allocation;
import Domain.Scheduler;
import Utils.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Preprocessing {
    private static final Logger logger = LoggerFactory.getLogger(Preprocessing.class);

    public static Scheduler generateDemoData() {
        ResourceNode resource1 = new ResourceNode();
        ResourceNode resource2 = new ResourceNode();
        ResourceNode resource3 = new ResourceNode();
        resource1.setId("resource1");
        resource2.setId("resource2");
        resource3.setId("resource3");
        resource1.getTimeSlots().add(new TimeSlot("shift11", 0L, 10L));
        resource1.getTimeSlots().add(new TimeSlot("shift12", 15L, 25L));
        resource1.getTimeSlots().add(new TimeSlot("shift13", 30L, 40L));
        resource2.getTimeSlots().add(new TimeSlot("shift21", 0L, 10L));
        resource2.getTimeSlots().add(new TimeSlot("shift22", 15L, 25L));
        resource2.getTimeSlots().add(new TimeSlot("shift23", 30L, 40L));
        resource3.getTimeSlots().add(new TimeSlot("shift31", 0L, 10L));
        resource3.getTimeSlots().add(new TimeSlot("shift32", 15L, 25L));
        resource3.getTimeSlots().add(new TimeSlot("shift33", 30L, 40L));
        Task task1 = new Task();
        task1.setId("task1");
        MasterDemand masterDemand = new MasterDemand();
        masterDemand.setId("masterDemand1");
        masterDemand.setBreachDate(12L);
        task1.setMasterDemands(new ArrayList<>(List.of(masterDemand)));
        Operation op1 = new Operation();
        Operation op2 = new Operation();
        Operation op3 = new Operation();
        Operation op4 = new Operation();
        op1.setId("op1");
        op2.setId("op2");
        op3.setId("op3");
        op4.setId("op4");

        op1.setFirst(true);
        op2.setFirst(true);

        op1.setQuantity(2);
        op2.setQuantity(2);
        op3.setQuantity(2);
        op4.setQuantity(2);
        op1.setOperationStartRelationShip(OperationStartRelationShip.ES);
        op2.setOperationStartRelationShip(OperationStartRelationShip.ES);
        op3.setOperationStartRelationShip(OperationStartRelationShip.ES);
        op4.setOperationStartRelationShip(OperationStartRelationShip.ES);
        op1.setParentTask(task1);
        op2.setParentTask(task1);
        op3.setParentTask(task1);
        op4.setParentTask(task1);
        op4.setLast(true);
        ExecutionMode mod1_1 = new ExecutionMode();
        ExecutionMode mod1_2 = new ExecutionMode();
        ExecutionMode mod2_1 = new ExecutionMode();
        ExecutionMode mod2_2 = new ExecutionMode();
        ExecutionMode mod3_1 = new ExecutionMode();
        ExecutionMode mod3_2 = new ExecutionMode();
        ExecutionMode mod4_1 = new ExecutionMode();
        mod1_1.setResourceRequirement(new ResourceRequirement("re1", resource1));
        mod1_2.setResourceRequirement(new ResourceRequirement("re2", resource2));
        mod2_1.setResourceRequirement(new ResourceRequirement("re3", resource1));
        mod2_2.setResourceRequirement(new ResourceRequirement("re4", resource2));
        mod3_1.setResourceRequirement(new ResourceRequirement("re5", resource2));
        mod3_2.setResourceRequirement(new ResourceRequirement("re6", resource3));
        mod4_1.setResourceRequirement(new ResourceRequirement("re7", resource3));
        mod1_1.setOperation(op1);
        mod1_1.setQuantityPerBeat(1);
        mod1_1.setBeat(3);
        mod1_2.setOperation(op1);
        mod1_2.setQuantityPerBeat(1);
        mod1_2.setBeat(3);
        mod2_1.setOperation(op2);
        mod2_1.setQuantityPerBeat(1);
        mod2_1.setBeat(3);
        mod2_2.setOperation(op2);
        mod2_2.setQuantityPerBeat(1);
        mod2_2.setBeat(3);
        mod3_1.setOperation(op3);
        mod3_1.setQuantityPerBeat(1);
        mod3_1.setBeat(3);
        mod3_2.setOperation(op3);
        mod3_2.setQuantityPerBeat(1);
        mod3_2.setBeat(3);
        mod4_1.setOperation(op4);
        mod4_1.setQuantityPerBeat(1);
        mod4_1.setBeat(3);
        op1.setExecutionModes(new ArrayList<>(List.of(mod1_1, mod1_2)));
        op2.setExecutionModes(new ArrayList<>(List.of(mod2_1, mod2_2)));
        op3.setExecutionModes(new ArrayList<>(List.of(mod3_1, mod3_2)));
        op4.setExecutionModes(new ArrayList<>(List.of(mod4_1)));
        op1.getNextOperations().add(op3);
        op2.getNextOperations().add(op3);
        op3.getPreviousOperations().add(op1);
        op3.getPreviousOperations().add(op2);
        op3.getNextOperations().add(op4);
        op4.getPreviousOperations().add(op3);
        task1.setCraftPath(new ArrayList<>(List.of(op1, op2, op3, op4)));

        // frozen
//        op1.setFrozen(true);
//        op1.setFrozenPrevious(resource2);
//
//        op2.setFrozen(true);
//        op2.setFrozenPrevious(op1);

        // Build void problem
        Scheduler problem = new Scheduler();
        problem.setId("solver");
        problem.setTasks(new ArrayList<>(List.of(task1)));
        problem.setResourceNodes(new ArrayList<>(List.of(resource1, resource2, resource3)));
        logger.info("Data loaded end");
        return problem;
    }

    public static void print(Scheduler solution) {
        for (ResourceNode resourceNode : solution.getResourceNodes()) {
            System.out.print("Resource Result:" + resourceNode.getId());
            if (resourceNode.getNext() != null) {
                Allocation allocation = resourceNode.getNext();
                while (allocation != null) {
                    System.out.print(" -> " + allocation.getOperation().getId() + ":" + "[" + allocation.getStartTime() + "," + allocation.getEndTime() + "]");
                    allocation = allocation.getNext();
                }
            }
            System.out.println();
        }

    }

    public static void DataCheck(Scheduler scheduler) {
        // Resource check
        List<ResourceNode> resourceNodes = scheduler.getResourceNodes();
        List<ResourceNode> toRemoveResources = new ArrayList<>();
        for (ResourceNode resource : resourceNodes) {
            if (resource.getTimeSlots().isEmpty()) {
                logger.warn("Resource {} has no time slots", resource.getId());
                toRemoveResources.add(resource);
            }
        }
        resourceNodes.removeAll(toRemoveResources);

        // Task check
        List<Task> toRemoveTasks = new ArrayList<>();
        for (Task task : scheduler.getTasks()) {
            if (!task.TaskDataCheck(resourceNodes)) {
                toRemoveTasks.add(task);
            } else {
                logger.info("Task {} pass data check", task.getId());
            }
        }
        scheduler.getTasks().removeAll(toRemoveTasks);

    }

    public static void calculateFrozenTask(Scheduler scheduler) {
        if (scheduler.getFrozenSeconds() == 0) {
            logger.warn("No freeze period is set");
            return;
        }
        HashMap<ResourceNode, List<Operation>> frozenMap = new HashMap<>();
        for (Task task : scheduler.getTasks()) {
            for (Operation operation : task.getCraftPath()) {
                if (operation.getPlannedResource() != null &&
                        operation.getPlannedStartTime() != null &&
                        operation.getPlannedStartTime() <= scheduler.getStartSchedulingTime() + scheduler.getFrozenSeconds()) {

                    frozenMap.computeIfAbsent(operation.getPlannedResource(), k -> new ArrayList<>()).add(operation);
                }
            }
        }
        for (List<Operation> operations : frozenMap.values()) {
            operations.sort(Comparator.comparing(Operation::getPlannedStartTime));
        }
        // TODO:DAG check
        for (Map.Entry<ResourceNode, List<Operation>> entry : frozenMap.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                entry.getValue().get(i).setFrozen(true);
                if (i == 0) {
                    entry.getValue().get(i).setFrozenPrevious(entry.getKey());
                } else {
                    entry.getValue().get(i).setFrozenPrevious(entry.getValue().get(i - 1));
                }
            }
        }
    }

    public static void BuildAllocations(Scheduler problem) {
        List<Allocation> allocations = new ArrayList<>();
        HashMap<Operation, Allocation> operationAllocationHashMap = new HashMap<>();

        for (Task task : problem.getTasks()) {
            for (Operation operation : task.getCraftPath()) {
                Allocation allocation = new Allocation();
                allocation.setId(RandomStringGenerator.generateRandomString(16));
                allocation.setOperation(operation);
                allocation.setAllResources(problem.getResourceNodes());
                allocations.add(allocation);
                operationAllocationHashMap.put(operation, allocation);
            }
        }
        for (Task task : problem.getTasks()) {
            for (Operation currentOperation : task.getCraftPath()) {
                if (!currentOperation.getPreviousOperations().isEmpty()) {
                    for (Operation preOperation : currentOperation.getPreviousOperations()) {
                        operationAllocationHashMap.get(currentOperation).getPredecessorsAllocations().add(operationAllocationHashMap.get(preOperation));
                    }
                }
                if (!currentOperation.getNextOperations().isEmpty()) {
                    for (Operation nextOperation : currentOperation.getNextOperations()) {
                        operationAllocationHashMap.get(currentOperation).getSuccessorsAllocations().add(operationAllocationHashMap.get(nextOperation));
                    }
                }
            }
        }
        problem.setAllocations(allocations);
        for (Allocation allocation : allocations) {
            allocation.setAllAllocations(allocations);
        }
    }
}
