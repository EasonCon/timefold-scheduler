package DataStruct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MasterDemand extends Labeled {
    private long requiredDate;
    private long deliveryDate;
    private long breachDate;
    private long promisedDate;
    private long prePromisedDate;

    public MasterDemand() {
        super(null);
    }
}
