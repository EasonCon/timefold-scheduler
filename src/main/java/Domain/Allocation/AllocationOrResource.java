package Domain.Allocation;

import DataStruct.Labeled;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AllocationOrResource extends Labeled {
    public AllocationOrResource() {
        super(null);
    }
}
