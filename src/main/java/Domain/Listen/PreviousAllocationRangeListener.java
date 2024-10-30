//package Domain.Listen;
//
//import DataStruct.ExecutionMode;
//import Domain.Allocation.Allocation;
//import Domain.Allocation.AllocationOrResource;
//import DataStruct.ResourceNode;
//import Domain.Scheduler;
//import ai.timefold.solver.core.api.domain.variable.VariableListener;
//import ai.timefold.solver.core.api.score.director.ScoreDirector;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class PreviousAllocationRangeListener implements VariableListener<Scheduler, Allocation> {
//    @Override
//    public void beforeVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//
//    }
//
//    @Override
//    public void afterVariableChanged(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//        updatePreviousAllocationRange(scoreDirector, allocation);
//    }
//
//    @Override
//    public void beforeEntityAdded(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//
//    }
//
//    @Override
//    public void afterEntityAdded(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//        updatePreviousAllocationRange(scoreDirector, allocation);
//    }
//
//    @Override
//    public void beforeEntityRemoved(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//
//    }
//
//    @Override
//    public void afterEntityRemoved(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//
//    }
//
//    public void updatePreviousAllocationRange(ScoreDirector<Scheduler> scoreDirector, Allocation allocation) {
//        // 1.Resource from operation execution mode.
//        // 2.Allocation from resource chained.
//        System.out.println(allocation.getId() + "变量监听开始,当前possiblePreviousAllocation:");
//        for (AllocationOrResource pre : allocation.getPossiblePreviousAllocation()) {
//            if (pre instanceof ResourceNode) {
//                System.out.println(((ResourceNode) pre).getId());
//            }
//            if (pre instanceof Allocation) {
//                System.out.println(((Allocation) pre).getId());
//            }
//        }
//        List<ResourceNode> resourceList = new ArrayList<>();
//        for (ExecutionMode executionMode : allocation.getOperation().getExecutionModes()) {
//            resourceList.add(executionMode.getResourceRequirement().getResourceNode());
//        }
//
//        List<AllocationOrResource> allocations = new ArrayList<>(resourceList);
//        for (ResourceNode resourceNode : resourceList) {
//            if (resourceNode.getNext() != null) {
//                Allocation cursor = resourceNode.getNext();
//                while (cursor != null) {
//                    if (!Objects.equals(cursor.getId(), allocation.getId())) {
//                        allocations.add(cursor);
//                    }
//                    cursor = cursor.getNext();
//                }
//            }
//        }
//
//        scoreDirector.beforeVariableChanged(allocation, "possiblePreviousAllocation");
//        allocation.setPossiblePreviousAllocation(allocations);
//        scoreDirector.afterVariableChanged(allocation, "possiblePreviousAllocation");
//
//        System.out.println("变量监听结束,变更后possiblePreviousAllocation:");
//        for (AllocationOrResource pre : allocation.getPossiblePreviousAllocation()) {
//            if (pre instanceof ResourceNode) {
//                System.out.println(((ResourceNode) pre).getId());
//            }
//            if (pre instanceof Allocation) {
//                System.out.println(((Allocation) pre).getId());
//            }
//        }
//
//    }
//}
