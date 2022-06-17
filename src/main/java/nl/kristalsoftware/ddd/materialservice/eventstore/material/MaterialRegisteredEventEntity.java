package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDBaseEventEntity;

import javax.persistence.Entity;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity(name = "MaterialRegisteredEvent")
public class MaterialRegisteredEventEntity extends UUIDBaseEventEntity<MaterialRegistered> {

    private String description;
    private Integer quantity;

    private MaterialRegisteredEventEntity(
            UUID reference,
            String domainEventName,
            String description,
            Integer quantity
    ) {
        super(reference, domainEventName);
        this.description = description;
        this.quantity = quantity;
    }

    public static MaterialRegisteredEventEntity of(MaterialRegistered materialRegistered) {
        return new MaterialRegisteredEventEntity(
            materialRegistered.getMaterialReference().getValue(),
            materialRegistered.getClass().getSimpleName(),
            materialRegistered.getMaterialDescription().getValue(),
            materialRegistered.getMaterialInStock().getValue()
        );
    }

    @Override
    public MaterialRegistered getDomainEvent() {
        return MaterialRegistered.of(
                MaterialReference.of(getReference()),
                MaterialDescription.of(description),
                MaterialQuantity.of(quantity)
        );
    }

}
