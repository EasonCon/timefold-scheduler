package DataStruct;

import Domain.Allocation.ResourceNode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResourceRequirement extends Labeled {

    private ResourceNode resourceNode;

    public ResourceRequirement() {
        super(null);
    }

    public ResourceRequirement(String id, ResourceNode resourceNode) {
        super(id);
        this.resourceNode = resourceNode;
    }
}
