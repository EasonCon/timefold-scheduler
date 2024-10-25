package DataStruct;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
public class Task extends Labeled {
    private String materialId;
    private String materialDescription;
    private float quantity;
    private List<Operation> craftPath = new ArrayList<>();
    private List<MasterDemand> masterDemands = new ArrayList<>();
    private SupplyType supplyType;
    private Long earliestStartTime;
    private Long latestStartTime;
    private Long materialSetTime;


    public Task() {
        super(null);
    }

    public boolean LegalityJudgment(){
        /*
         * 1. Ring detection
         * 2. Operation resource check
         */
        return true;
    }
}
