package DataStruct.Resource;

import DataStruct.Labeled;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceRequirement extends Labeled {
    private AmountLimitedResource amountLimitedResource;
    private RenewableResource renewableResource;
    private UnlimitedResource unlimitedResource;

    // constructor
    public ResourceRequirement() {
        super(null);
    }

    public ResourceRequirement(String id, AmountLimitedResource amountLimitedResource, RenewableResource renewableResource, UnlimitedResource unlimitedResource){
        super(id);
        this.amountLimitedResource = amountLimitedResource;
        this.renewableResource = renewableResource;
        this.unlimitedResource = unlimitedResource;

        if (amountLimitedResource == null && renewableResource == null && unlimitedResource == null) {
            throw new IllegalArgumentException("ResourceRequirement must have at least one of amountLimitedResource, renewableResource, or unlimitedResource");
        }
    }
}
