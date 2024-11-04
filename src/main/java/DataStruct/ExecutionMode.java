package DataStruct;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExecutionMode extends Labeled {
    @NotNull
    private Operation operation;
    @NotNull
    private ResourceRequirement resourceRequirement;
    private Integer priority;

    @NotNull
    private float beat;
    @NotNull
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
