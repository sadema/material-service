package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class TicketMaterialView {
    private final MaterialQuantity reserved;
    private final MaterialQuantity used;

    public static TicketMaterialView of(MaterialQuantity reserved, MaterialQuantity used) {
        return new TicketMaterialView(reserved, used);
    }
}
