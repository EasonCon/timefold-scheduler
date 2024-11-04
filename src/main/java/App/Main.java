package App;


import DataStruct.*;
import Domain.Allocation.Allocation;
import DataStruct.ResourceNode;
import Domain.Scheduler;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String filePath = "data.xlsx";
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        Scheduler problem = generateData();
        SolverFactory<Scheduler> solverFactory = SolverFactory.createFromXmlResource("apsDemoConfig.xml");
        Solver<Scheduler> solver = solverFactory.buildSolver();
        Scheduler solution = solver.solve(problem);
        print(solution);
    }

    public static void print(Scheduler solution) {
        for (ResourceNode resourceNode : solution.getResourceNodes()) {
            System.out.print("资源结果:" + resourceNode.getId());
            if (resourceNode.getNext() != null) {
                Allocation allocation = resourceNode.getNext();
                while (allocation != null) {
                    System.out.print(" -> " + allocation.getId() + ":" + "[" + allocation.getStartTime() + "," + allocation.getEndTime() + "]");
                    allocation = allocation.getNext();
                }
            }
            System.out.println();
        }

    }

    public static Scheduler generateData() {
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
        op1.setQuantity(2);
        op2.setQuantity(2);
        op3.setQuantity(2);
        op4.setQuantity(2);
        op1.setOperationStartRelationShip(OperationStartRelationShip.ES);
        op2.setOperationStartRelationShip(OperationStartRelationShip.ES);
        op3.setOperationStartRelationShip(OperationStartRelationShip.ES);
        op4.setOperationStartRelationShip(OperationStartRelationShip.ES);

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
        logger.info("Data loaded end");

        // 构建solution
        Allocation allocation1 = new Allocation();
        allocation1.setId("allocation1");
        allocation1.setOperation(op1);


        Allocation allocation2 = new Allocation();
        allocation2.setId("allocation2");
        allocation2.setOperation(op2);

        Allocation allocation3 = new Allocation();
        allocation3.setId("allocation3");
        allocation3.setOperation(op3);

        Allocation allocation4 = new Allocation();
        allocation4.setId("allocation4");
        allocation4.setOperation(op4);


        allocation1.setSuccessorsAllocations(new ArrayList<>(List.of(allocation3)));
        allocation2.setSuccessorsAllocations(new ArrayList<>(List.of(allocation3)));
        allocation3.setPredecessorsAllocations(new ArrayList<>(List.of(allocation1, allocation2)));
        allocation3.setSuccessorsAllocations(new ArrayList<>(List.of(allocation4)));
        allocation4.setPredecessorsAllocations(new ArrayList<>(List.of(allocation3)));

        allocation1.setAllResources(new ArrayList<>(List.of(resource1, resource2, resource3)));
        allocation2.setAllResources(new ArrayList<>(List.of(resource1, resource2, resource3)));
        allocation3.setAllResources(new ArrayList<>(List.of(resource1, resource2, resource3)));
        allocation4.setAllResources(new ArrayList<>(List.of(resource1, resource2, resource3)));

        Scheduler problem = new Scheduler();
        problem.setId("sch1");
        problem.setAllocations(new ArrayList<>(List.of(allocation1, allocation2, allocation3, allocation4)));
        problem.setResourceNodes(new ArrayList<>(List.of(resource1, resource2, resource3)));

        return problem;

    }
}