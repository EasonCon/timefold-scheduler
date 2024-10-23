package DataStruct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSlot {
    private String shiftID;
    private long start;
    private long end;

    public TimeSlot() {
    }

    public TimeSlot(String shiftID, long start, long end) {
        this.shiftID = shiftID;
        this.start = start;
        this.end = end;
    }

    public long getWorkingSeconds() {
        return end - start;
    }
}

