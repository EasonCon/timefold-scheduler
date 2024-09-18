package base_domain;

import java.util.List;

public class Operation extends Labeled{
    private String name;
    private int order;
    private float minutesPerBeat;
    private float quantityPerBeat;
    private List<Resource> availableResources;
    private Task parentTask;
    private Operation perviousOperation;

    public Operation(String id, String name, int order, float minutesPerBeat, float quantityPerBeat, List<Resource> availableResources) {
        super(id);
        this.name = name;
        this.order = order;
        this.minutesPerBeat = minutesPerBeat;
        this.quantityPerBeat = quantityPerBeat;
        this.availableResources = availableResources;
    }

    public Operation(String id) {
        super(id);
    }

    public Operation() {
        super(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public float getMinutesPerBeat() {
        return minutesPerBeat;
    }

    public void setMinutesPerBeat(float minutesPerBeat) {
        this.minutesPerBeat = minutesPerBeat;
    }

    public float getQuantityPerBeat() {
        return quantityPerBeat;
    }

    public void setQuantityPerBeat(float quantityPerBeat) {
        this.quantityPerBeat = quantityPerBeat;
    }

    public List<Resource> getAvailableResources() {
        return availableResources;
    }

    public void setAvailableResources(List<Resource> availableResources) {
        this.availableResources = availableResources;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public Operation getPerviousOperation() {
        return perviousOperation;
    }

    public void setPreviousOperation(Operation previousOperation) {
        this.perviousOperation = previousOperation;
    }
}
