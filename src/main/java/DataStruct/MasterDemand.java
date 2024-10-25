package DataStruct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MasterDemand extends Labeled {
    private Long requiredDate;
    private Long deliveryDate;
    private Long breachDate;
    private Long promisedDate;
    private Long prePromisedDate;

    public MasterDemand() {
        super(null);
    }
}
