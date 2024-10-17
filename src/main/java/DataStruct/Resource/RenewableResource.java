package DataStruct.Resource;

import DataStruct.TimeSlot;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RenewableResource extends ResourceBase {
    private int capacity;
    private List<TimeSlot> timeSlots;

    public RenewableResource() {
    }

    public RenewableResource(String id, String name, String resourceGroup, String productionLine, List<TimeSlot> timeSlots) {
        super(id, name, resourceGroup, productionLine);
        this.timeSlots = timeSlots;
    }
}