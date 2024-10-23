package App;


import DataStruct.*;
import Domain.Allocation.Allocation;
import Domain.Allocation.AllocationOrResource;
import Domain.Allocation.ResourceNode;
import Domain.Scheduler;
import Domain.SchedulerConstraintProvider;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import ai.timefold.solver.core.config.constructionheuristic.ConstructionHeuristicType;
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
        ResourceNode resource1 = new ResourceNode();
        ResourceNode resource2 = new ResourceNode();
        resource1.setId("resource1");
        resource2.setId("resource2");
        resource1.getTimeSlots().add(new TimeSlot("shift1", 0L, 10L));


        Task task = new Task();
        task.setId("task1");

        Operation op1 = new Operation();
        Operation op2 = new Operation();

        ExecutionMode mod1_1 = new ExecutionMode();
        ExecutionMode mod1_2 = new ExecutionMode();
        ExecutionMode mod2_1 = new ExecutionMode();
        ExecutionMode mod2_2 = new ExecutionMode();

        mod1_1.setResourceRequirement(new ResourceRequirement("re", resource1));
        mod1_2.setResourceRequirement(new ResourceRequirement("re", resource2));
        mod2_1.setResourceRequirement(new ResourceRequirement("re", resource1));
        mod2_2.setResourceRequirement(new ResourceRequirement("re", resource2));

        mod1_1.setOperation(op1);
        mod1_2.setOperation(op1);
        mod2_1.setOperation(op2);
        mod2_2.setOperation(op2);

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
        allocation1.setResourceNode(resource1);
        allocation2.setResourceNode(resource2);

        allocation1.setPrevious(resource1);
        resource1.setNext(allocation1);

        allocation2.setPrevious(allocation1);
        allocation1.setNext(allocation2);

        Scheduler problem = new Scheduler();
        problem.setId("sch1");
        problem.setAllocations(new ArrayList<>(List.of(allocation1, allocation2)));
        problem.setResourceNodes(new ArrayList<>(List.of(resource1, resource2)));

        SolverFactory<Scheduler> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(Scheduler.class)
                .withEntityClasses(Allocation.class, AllocationOrResource.class)
                .withConstraintProviderClass(SchedulerConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(2)));


        Solver<Scheduler> solver = solverFactory.buildSolver();
        Scheduler solution = solver.solve(problem);

    }
}