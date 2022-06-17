package nl.kristalsoftware.ddd.materialservice.viewstore.material;

import lombok.Data;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.TicketMaterialView;

@Data
public class TicketMaterialDocumentPart {
    private Integer reserved;
    private Integer used;

    public TicketMaterialDocumentPart() {
        reserved = 0;
        used = 0;
    }

    public TicketMaterialView convert() {
        return TicketMaterialView.of(
                MaterialQuantity.of(reserved),
                MaterialQuantity.of(used)
        );
    }
}
