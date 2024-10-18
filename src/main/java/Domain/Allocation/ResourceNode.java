package Domain.Allocation;

import DataStruct.Resource.RenewableResource;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNode extends AllocationOrResource {
    private String id;
    private RenewableResource resource;
    private AllocationOrResource next;

    public ResourceNode() {

    }

    public ResourceNode(String id, RenewableResource resource, AllocationOrResource next) {
        this.id = id;
        this.resource = resource;
        this.next = next;
    }
}
