package DataStruct.Resource;

import lombok.Getter;
import lombok.Setter;

/*
有数量限制资源
 */
@Getter
@Setter
public class AmountLimitedResource extends ResourceBase {
    private float amount;

    public AmountLimitedResource(String id, String name, String resourceGroup, String productionLine, float amount) {
        super(id, name, resourceGroup, productionLine);
        this.amount = amount;
    }
}
