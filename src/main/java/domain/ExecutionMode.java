package domain;

import base_domain.Labeled;
import base_domain.Resource;

public class ExecutionMode extends Labeled {
    private int priority;
    private Resource resource;

    public ExecutionMode(String id, int priority, Resource resource) {
        super(id);
        this.priority = priority;
        this.resource = resource;
    }

    public ExecutionMode(String id) {
        super(id);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
