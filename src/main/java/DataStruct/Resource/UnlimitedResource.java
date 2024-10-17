package DataStruct.Resource;

/*
Such as Mould
 */
public class UnlimitedResource extends ResourceBase {
    public UnlimitedResource() {
        super(null, null, null, null);
    }

    public UnlimitedResource(String id, String name, String resourceGroup, String productionLine) {
        super(id, name, resourceGroup, productionLine);
    }
}
