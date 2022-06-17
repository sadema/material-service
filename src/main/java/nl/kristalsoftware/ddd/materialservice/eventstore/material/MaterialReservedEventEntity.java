package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDBaseEventEntity;

import javax.persistence.Entity;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity(name = "MaterialReservedEvent")
public class MaterialReservedEventEntity extends UUIDBaseEventEntity<MaterialReserved> {

    private Integer reservedQuantity;
    private UUID ticketReference;

    private MaterialReservedEventEntity(
            UUID materialReference,
            String domainEventName,
            Integer reservedQuantity,
            UUID ticketReference
    ) {
        super(materialReference, domainEventName);
        this.reservedQuantity = reservedQuantity;
        this.ticketReference = ticketReference;
    }

    public static MaterialReservedEventEntity of(MaterialReserved materialReserved) {
        return new MaterialReservedEventEntity(
                materialReserved.getMaterialReference().getValue(),
                materialReserved.getClass().getSimpleName(),
                materialReserved.getReservedQuantity().getValue(),
                materialReserved.getTicketReference().getValue()
        );
    }

    @Override
    public MaterialReserved getDomainEvent() {
        return MaterialReserved.of(
                MaterialReference.of(getReference()),
                MaterialQuantity.of(reservedQuantity),
                TicketReference.of(ticketReference)
        );
    }

}
