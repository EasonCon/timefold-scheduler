package Domain.Allocation;

import DataStruct.TimeSlot;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ResourceNode extends AllocationOrResource {

    private String id;
    private String name;
    private List<TimeSlot> timeSlots;

    public ResourceNode() {

    }
}
