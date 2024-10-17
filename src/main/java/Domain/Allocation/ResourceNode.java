package Domain.Allocation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNode extends AllocationOrResource {
    private AllocationOrResource next;

    public ResourceNode() {
    }
}
