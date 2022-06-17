package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDBaseEventEntity;

import javax.persistence.Entity;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity(name = "MaterialUsedEvent")
public class MaterialUsedEventEntity extends UUIDBaseEventEntity<MaterialUsed> {

    private Integer usedQuantity;
    private UUID ticketReference;

    private MaterialUsedEventEntity(
            UUID materialReference,
            String domainEventName,
            Integer usedQuantity,
            UUID ticketReference) {
        super(materialReference, domainEventName);
        this.usedQuantity = usedQuantity;
        this.ticketReference = ticketReference;
    }

    public static MaterialUsedEventEntity of(MaterialUsed materialUsed) {
        return new MaterialUsedEventEntity(
                materialUsed.getMaterialReference().getValue(),
                materialUsed.getClass().getSimpleName(),
                materialUsed.getUsedQuantity().getValue(),
                materialUsed.getTicketReference().getValue()
        );
    }

    @Override
    public MaterialUsed getDomainEvent() {
        return MaterialUsed.of(
                MaterialReference.of(getReference()),
                MaterialQuantity.of(usedQuantity),
                TicketReference.of(ticketReference)
        );
    }
}
