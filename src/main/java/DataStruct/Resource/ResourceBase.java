package DataStruct.Resource;

import DataStruct.Labeled;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ResourceBase extends Labeled {
    private String name;
    private String resourceGroup;
    private String productionLine;

    public ResourceBase() {
        super(null);
    }

    public ResourceBase(String id, String name, String resourceGroup, String productionLine) {
        super(id);
        this.name = name;
        this.resourceGroup = resourceGroup;
        this.productionLine = productionLine;
    }
}
