package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Data
public class MaterialDomainEntity {

    private MaterialReference materialReference;
    private MaterialDescription materialDescription;
    private MaterialQuantity materialInStock;
    private Map<TicketReference,MaterialQuantity> materialReservedByTicket;
    private MaterialQuantity materialUsed;
    private MaterialQuantity materialSentBack;

    public MaterialDomainEntity() {
        materialDescription = MaterialDescription.of("");
        materialInStock = MaterialQuantity.of(0);
        materialReservedByTicket = new HashMap<>();
        materialUsed = MaterialQuantity.of(0);
        materialSentBack = MaterialQuantity.of(0);
    }

//    public static MaterialDomainEntity of(
//            MaterialReference materialReference,
//            MaterialDescription materialDescription,
//            MaterialQuantity materialItemsInStock,
//            MaterialQuantity materialItemsReserved,
//            MaterialQuantity materialItemsUsed
//    ) {
//        return new MaterialDomainEntity(
//                materialReference,
//                materialDescription,
//                materialItemsInStock,
//                materialItemsReserved,
//                materialItemsUsed
//        );
//    }
}
