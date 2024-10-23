package DataStruct;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExecutionMode extends Labeled {
    private Operation operation;
    private ResourceRequirement resourceRequirement;
    private int priority;
    private float beat;
    private float quantityPerBeat;


    public ExecutionMode() {
        super(null);
    }

    public long getDuration() {
        long duration = 0L;
        duration = (long) (this.beat * this.operation.getQuantity() / this.quantityPerBeat);
        return duration;
    }
}
