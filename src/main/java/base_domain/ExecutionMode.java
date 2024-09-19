package base_domain;


public class ExecutionMode extends Labeled {
    private Operation operation;
    private Resource resource;
    private int priority;


    public ExecutionMode(String id) {
        super(id);
    }

    public ExecutionMode(String id, Operation operation, Resource resource, int priority) {
        super(id);
        this.operation = operation;
        this.resource = resource;
        this.priority = priority;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
