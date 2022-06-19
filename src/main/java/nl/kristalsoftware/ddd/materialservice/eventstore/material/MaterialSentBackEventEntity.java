package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentRetour;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDBaseEventEntity;

import javax.persistence.Entity;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity(name = "MaterialSentBackEvent")
public class MaterialSentBackEventEntity extends UUIDBaseEventEntity<MaterialSentRetour> {

    private Integer sentBackQuantity;
    private UUID ticketReference;

    private MaterialSentBackEventEntity(
            UUID materialReference,
            String domainEventName,
            Integer sentBackQuantity,
            UUID ticketReference) {
        super(materialReference, domainEventName);
        this.sentBackQuantity = sentBackQuantity;
        this.ticketReference = ticketReference;
    }

    public static MaterialSentBackEventEntity of(MaterialSentRetour materialSentRetour) {
        return new MaterialSentBackEventEntity(
                materialSentRetour.getMaterialReference().getValue(),
                materialSentRetour.getClass().getSimpleName(),
                materialSentRetour.getSentBackQuantity().getValue(),
                materialSentRetour.getTicketReference().getValue()
        );
    }

    @Override
    public MaterialSentRetour getDomainEvent() {
        return MaterialSentRetour.of(
                MaterialReference.of(getReference()),
                MaterialQuantity.of(sentBackQuantity),
                TicketReference.of(ticketReference)
        );
    }
}
