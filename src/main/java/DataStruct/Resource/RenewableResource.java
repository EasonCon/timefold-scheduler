package DataStruct.Resource;

import DataStruct.TimeSlot;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
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

    public Long getEndTime(Long startTime, long duration) {
        long remainingTime = duration;
        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.getEnd() < startTime) {
                continue;
            } else {
                long thisTimeSlotDuration = timeSlot.getEnd() - startTime;
                if (thisTimeSlotDuration < remainingTime) {
                    remainingTime -= thisTimeSlotDuration;
                    startTime = timeSlot.getEnd();
                    if(startTime == this.timeSlots.getLast().getEnd()){
                        return null;
                    }
                } else {
                    return startTime + remainingTime;
                }
            }
        }
        return null;
    }
}