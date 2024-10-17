package App;

import DataStruct.Operation;
import DataStruct.OperationStartRelationShip;
import DataStruct.Resource.RenewableResource;
import DataStruct.Resource.ResourceRequirement;
import DataStruct.Task;
import DataStruct.ExecutionMode;
import Utils.RandomStringGenerator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String filePath = "data.xlsx";

    public static void main(String[] args) {
        int taskCount = 2;
        int resourceCount = 5;
        List<Task> taskList = new ArrayList<>();
        List<RenewableResource> resourceList = new ArrayList<>();

        // 构造资源
        for (int i = 0; i < resourceCount; i++) {
            RenewableResource resource = new RenewableResource();
            resource.setId(RandomStringGenerator.generateRandomString(16));
            resource.setName("资源" + i);
            resource.setCapacity(1);
            resourceList.add(resource);
        }


        for (int i = 0; i < taskCount; i++) {
            Task task = new Task();
            task.setId("任务" + i);
            task.setMaterialId("材料" + i);
            task.setQuantity(5);

            // 构造这个任务的工序
            List<Operation> operationList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Operation operation = new Operation();
                operation.setId(RandomStringGenerator.generateRandomString(16));
                operation.setName("工序" + j);
                operation.setOrder(j);
                operation.setParentTask(task);
                operation.setOperationStartRelationShip(OperationStartRelationShip.ES);
                operation.setFirst(false);
                operation.setLast(false);

                // 构造这个工序的执行方式
                List<ExecutionMode> executionModes = new ArrayList<>();

                // mode 1
                ExecutionMode mode1 = new ExecutionMode();
                mode1.setId(RandomStringGenerator.generateRandomString(16));
                mode1.setOperation(operation);
                mode1.setResourceRequirement(new ResourceRequirement(RandomStringGenerator.generateRandomString(16), null, resourceList.getFirst(), null));
                mode1.setPriority(1);
                mode1.setBeat(5);
                mode1.setQuantityPerBeat(1);
                executionModes.add(mode1);

                // mode 2
                ExecutionMode mode2 = new ExecutionMode();
                mode2.setId(RandomStringGenerator.generateRandomString(16));
                mode2.setOperation(operation);
                mode2.setResourceRequirement(new ResourceRequirement(RandomStringGenerator.generateRandomString(16), null, resourceList.get(1), null));
                mode2.setPriority(2);
                mode2.setBeat(10);
                mode2.setQuantityPerBeat(1);
                executionModes.add(mode2);

                operation.setExecutionModes(executionModes);
                if (j == 0) {
                    operation.setFirst(true);
                }
                if (j == 2) {
                    operation.setLast(true);
                }

                operationList.add(operation);
            }

            // 构建工序关联
            for (int j = 0; j < operationList.size() - 1; j++) {
                operationList.get(j).getNextOperations().add(operationList.get(j + 1));
                operationList.get(j + 1).getPreviousOperations().add(operationList.get(j));

                task.setCraftPath(operationList);
            }
            taskList.add(task);
        }
        System.out.println();
    }
}