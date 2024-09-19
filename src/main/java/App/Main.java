package App;

import FJSP.*;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import base_domain.ExecutionMode;
import base_domain.Operation;
import base_domain.Resource;
import base_domain.Task;
import score.MinimizeTimeConstraints;
import utils.ExcelDataLoader;
import utils.RandomStringGenerator;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Main {
    private static final String filePath = "data.xlsx";

    public static void main(String[] args) throws IOException {
        ExcelDataLoader loader = new ExcelDataLoader(filePath);
        List<Task> tasks = loader.loadTasks();
        List<Resource> resources = loader.loadResources();

        SourceAllocation sourceAllocation = new SourceAllocation(RandomStringGenerator.generateRandomString(16));
        SinkAllocation sinkAllocation = new SinkAllocation(RandomStringGenerator.generateRandomString(16));

        List<OperationAllocation> allOperationAllocations = new ArrayList<>();
        HashMap<Task, List<OperationAllocation>> taskOperationAllocation = new LinkedHashMap<>();

        for (Task task : tasks) {
            List<OperationAllocation> thisTaskOperationAllocation = new ArrayList<>();
            for (Operation operation : task.getCraftPath()) {
                List<ExecutionMode> executionModes = new ArrayList<>();  // 当前工序的执行模式
                List<Resource> availableResources = operation.getAvailableResources();
                for (int i = 0; i < availableResources.size(); i++) {
                    Resource resource = availableResources.get(i);
                    ExecutionMode executionMode = new ExecutionMode(RandomStringGenerator.generateRandomString(16), operation, resource, i);
                    executionModes.add(executionMode);
                }
                // 构建分配关系
                OperationAllocation operationAllocation = new OperationAllocation(RandomStringGenerator.generateRandomString(16));
                operationAllocation.setOperation(operation);
                operationAllocation.setExecutionModes(executionModes);
                operationAllocation.setAssignedExecutionMode(executionModes.getFirst());  // 默认选择第一个执行模式
                thisTaskOperationAllocation.add(operationAllocation);
            }
            taskOperationAllocation.put(task, thisTaskOperationAllocation);
            allOperationAllocations.addAll(thisTaskOperationAllocation);
        }

        allOperationAllocations.getFirst().setPreviousOperationAllocation(sourceAllocation);
        allOperationAllocations.getFirst().setNextOperationAllocation(allOperationAllocations.get(1));
        allOperationAllocations.getLast().setNextOperationAllocation(sinkAllocation);
        allOperationAllocations.getLast().setPreviousOperationAllocation(allOperationAllocations.get(allOperationAllocations.size() - 2));

        for (int i = 1; i < allOperationAllocations.size() - 1; i++) {
            allOperationAllocations.get(i).setPreviousOperationAllocation(allOperationAllocations.get(i - 1));
            allOperationAllocations.get(i).setNextOperationAllocation(allOperationAllocations.get(i + 1));
        }

        Solution solution = new Solution();
        solution.setId(RandomStringGenerator.generateRandomString(16));
        solution.setTaskList(tasks);
        solution.setResourceList(resources);
        solution.setOperationAllocationList(allOperationAllocations);
        solution.setOperationAllocationMap(taskOperationAllocation);
        solution.setSourceAllocation(sourceAllocation);
        solution.setSinkAllocation(sinkAllocation);

        // debug
        solution.calculateTimes();

        SolverFactory<Solution> solverFactory = SolverFactory.create(
                new SolverConfig()
                        .withSolutionClass(Solution.class)
                        .withEntityClasses(OperationAllocation.class)
                        .withConstraintProviderClass(MinimizeTimeConstraints.class)
                        .withTerminationSpentLimit(Duration.ofSeconds(10)));

        Solver<Solution> solver = solverFactory.buildSolver();
        Solution solve = solver.solve(solution);
    }
}