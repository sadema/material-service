package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor(staticName = "of")
public class MaterialView {

    private MaterialReference materialReference;
    private MaterialDescription materialDescription;
    private MaterialQuantity materialItemsInStock;
    private Set<Map.Entry<TicketReference, TicketMaterialView>> materialByTicket;

}
