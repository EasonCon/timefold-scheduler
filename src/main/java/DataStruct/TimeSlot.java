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

    public  long getWorkingSeconds() {
        return end - start;
    }
}

