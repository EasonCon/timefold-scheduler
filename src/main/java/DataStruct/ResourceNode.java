package DataStruct;

import Domain.Allocation.AllocationOrResource;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ResourceNode extends AllocationOrResource {
    private String name;
    private List<TimeSlot> timeSlots = new ArrayList<>();

    public ResourceNode() {
    }

    public Long getEndTime(Long startTime, long duration) {
        if (startTime == null || startTime >= this.getTimeSlots().getLast().getEnd()) {
            return null;
        }
        long remainingDuration = duration;
        long endTime = this.getTimeSlots().getLast().getEnd();  // No reassignment, indicating that it has reached the rightmost
        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.getEnd() <= startTime) {
                continue;
            }
            long slotStart = Math.max(startTime, timeSlot.getStart());
            if (slotStart + remainingDuration <= timeSlot.getEnd()) {
                endTime = slotStart + remainingDuration;
                break;
            } else {
                remainingDuration -= timeSlot.getEnd() - slotStart;
            }
        }
        return endTime;
    }

    public Long getValidStartTime(Long startTime) {
        if (startTime == null || startTime >= this.getTimeSlots().getLast().getEnd()) {
            return null;
        }
        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.getStart() <= startTime && timeSlot.getEnd() >= startTime) {
                return timeSlot.getStart();
            }
        }
        return null;
    }
}