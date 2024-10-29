package App;


import DataStruct.*;
import Domain.Allocation.Allocation;
import Domain.Allocation.AllocationOrResource;
import Domain.Allocation.ResourceNode;
import Domain.DAGLoopDetectionFilter;
import Domain.Scheduler;
import Domain.SchedulerConstraintProvider;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import ai.timefold.solver.core.config.constructionheuristic.ConstructionHeuristicType;
import ai.timefold.solver.core.config.heuristic.selector.entity.EntitySelectorConfig;
import ai.timefold.solver.core.config.heuristic.selector.move.MoveSelectorConfig;
import ai.timefold.solver.core.config.localsearch.LocalSearchPhaseConfig;
import ai.timefold.solver.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
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
                    System.out.print(" -> " + allocation.getId() + ":" + allocation.getStartTime());
                    allocation = allocation.getNext();
                }
            }
            System.out.println();
        }

    }

    public static Scheduler generateData() {
        ResourceNode resource1 = new ResourceNode();
        ResourceNode resource2 = new ResourceNode();
        resource1.setId("resource1");
        resource2.setId("resource2");
        resource1.getTimeSlots().add(new TimeSlot("shift11", 0L, 10L));
        resource1.getTimeSlots().add(new TimeSlot("shift12", 15L, 25L));

        resource2.getTimeSlots().add(new TimeSlot("shift21", 0L, 10L));
        resource2.getTimeSlots().add(new TimeSlot("shift22", 15L, 25L));


        Task task = new Task();
        task.setId("task1");
        MasterDemand masterDemand = new MasterDemand();
        masterDemand.setId("masterDemand1");
        masterDemand.setBreachDate(12L);
        task.setMasterDemands(new ArrayList<>(List.of(masterDemand)));

        Operation op1 = new Operation();
        Operation op2 = new Operation();
        op1.setId("op1");
        op2.setId("op2");
        op1.setQuantity(2);
        op2.setQuantity(3);
        op1.setOperationStartRelationShip(OperationStartRelationShip.ES);
        op2.setOperationStartRelationShip(OperationStartRelationShip.ES);

        ExecutionMode mod1_1 = new ExecutionMode();
        ExecutionMode mod1_2 = new ExecutionMode();
        ExecutionMode mod2_1 = new ExecutionMode();
        ExecutionMode mod2_2 = new ExecutionMode();

        mod1_1.setResourceRequirement(new ResourceRequirement("re", resource1));
        mod1_2.setResourceRequirement(new ResourceRequirement("re", resource2));
        mod2_1.setResourceRequirement(new ResourceRequirement("re", resource1));
        mod2_2.setResourceRequirement(new ResourceRequirement("re", resource2));

        mod1_1.setOperation(op1);
        mod2_1.setOperation(op2);
        mod1_1.setBeat(2);
        mod1_2.setBeat(3);
        mod1_1.setQuantityPerBeat(1);
        mod2_1.setQuantityPerBeat(1);

        mod1_2.setOperation(op1);
        mod2_2.setOperation(op2);
        mod1_2.setBeat(2);
        mod2_2.setBeat(3);
        mod1_2.setQuantityPerBeat(1);
        mod2_2.setQuantityPerBeat(1);

        op1.setExecutionModes(new ArrayList<>(List.of(mod1_1, mod1_2)));
        op2.setExecutionModes(new ArrayList<>(List.of(mod2_1, mod2_2)));
        op1.getNextOperations().add(op2);
        op2.getPreviousOperations().add(op1);

        task.setCraftPath(new ArrayList<>(List.of(op1, op2)));
        logger.info("Data loaded end");

        // 构建solution
        Allocation allocation1 = new Allocation();
        allocation1.setId("allocation1");
        allocation1.setOperation(op1);


        Allocation allocation2 = new Allocation();
        allocation2.setId("allocation2");
        allocation2.setOperation(op2);


        allocation1.setSuccessorsAllocations(new ArrayList<>(List.of(allocation2)));
        allocation2.setPredecessorsAllocations(new ArrayList<>(List.of(allocation1)));

        allocation1.setAllResources(new ArrayList<>(List.of(resource1, resource2)));
        allocation2.setAllResources(new ArrayList<>(List.of(resource1, resource2)));

//        allocation1.setResourceNode(resource1);
//        allocation2.setResourceNode(resource2);
//        allocation1.setPrevious(resource1);
//        resource1.setNext(allocation1);
//        allocation2.setPrevious(resource2);
//        resource2.setNext(allocation2);

        Scheduler problem = new Scheduler();
        problem.setId("sch1");
        problem.setAllocations(new ArrayList<>(List.of(allocation1, allocation2)));
        problem.setResourceNodes(new ArrayList<>(List.of(resource1, resource2)));

        return problem;

    }
}