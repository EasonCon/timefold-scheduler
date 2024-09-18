package App;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import base_domain.Operation;
import base_domain.Resource;
import base_domain.Task;
import domain.*;
import solver.TimeConstrainsProvider;
import utils.ExcelDataLoader;
import utils.RandomStringGenerator;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String filePath = "data.xlsx";

    public static void main(String[] args) throws IOException {
        ExcelDataLoader loader = new ExcelDataLoader(filePath);
        List<Task> tasks = loader.loadTasks();
        List<SequenceNode> sequenceNodes = new ArrayList<>();

        // dummy node
        Head head = new Head(RandomStringGenerator.generateRandomString(16));
        Tail tail = new Tail(RandomStringGenerator.generateRandomString(16));

        // 构建nodes
        for (Task task : tasks) {
            SequenceNode node = new SequenceNode(RandomStringGenerator.generateRandomString(16));
            node.setTask(task);
            if (sequenceNodes.isEmpty()) {
                node.setPreviousNode(head);
            } else {
                node.setPreviousNode(sequenceNodes.getLast());
                sequenceNodes.getLast().setNextNode(node);  // shadow
            }

            // node -> allocations
            List<OperationAllocation> allocations = new ArrayList<>();
            node.setOperationAllocations(allocations);

            // 构建工序分配关系
            for (Operation operation : task.getCraftPath()) {
                OperationAllocation allocation = new OperationAllocation(RandomStringGenerator.generateRandomString(16));
                allocation.setNode(node);
                allocation.setOperation(operation);

                // 构造执行模式
                List<ExecutionMode> executionModes = new ArrayList<>();
                for (Resource resource : operation.getAvailableResources()) {
                    ExecutionMode executionMode = new ExecutionMode(RandomStringGenerator.generateRandomString(16));
                    executionMode.setResource(resource);
                    executionMode.setPriority(operation.getAvailableResources().indexOf(resource));
                    executionModes.add(executionMode);
                }
                // 如果为空，抛出异常
                if(executionModes.isEmpty()){
                    throw new IllegalArgumentException("Execution modes list cannot be empty.");
                }
                allocation.setExecutionModes(executionModes);
                allocation.setAssignedExecutionMode(executionModes.getFirst());  // 默认最高优先级

                // 如果是第一个工序
                if (operation.getParentTask().getCraftPath().indexOf(operation) == 0) {
                    allocation.setPreviousOperationAllocation(null);
                } else {
                    allocation.setPreviousOperationAllocation(allocation.getNode().getOperationAllocations().getLast());
                }

                allocations.add(allocation);

            }
            sequenceNodes.add(node);
        }

        sequenceNodes.getLast().setNextNode(tail);  // 指向tail

        // 构建solution
        Solution problem = new Solution();
        problem.setTaskList(tasks);
        problem.setSequenceNodeList(sequenceNodes);
        problem.setHead(head);
        problem.setTail(tail);

        SolverFactory<Solution> solverFactory = SolverFactory.create(
                new SolverConfig()
                .withSolutionClass(Solution.class)
                .withEntityClasses(OperationAllocation.class)
                .withEntityClasses(SequenceNode.class)
                .withConstraintProviderClass(TimeConstrainsProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(10))
        );

        Solver<Solution> solver = solverFactory.buildSolver();
        Solution solution = solver.solve(problem);
    }
}