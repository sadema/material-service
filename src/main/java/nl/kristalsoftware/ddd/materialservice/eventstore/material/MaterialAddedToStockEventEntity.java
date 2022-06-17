package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDBaseEventEntity;

import javax.persistence.Entity;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity(name = "MaterialAddedToStockEvent")
public class MaterialAddedToStockEventEntity extends UUIDBaseEventEntity<MaterialStockChanged> {

    private Integer addedToStockQuantity;

    private MaterialAddedToStockEventEntity(
            UUID reference,
            String domainEventName,
            Integer addedToStockQuantity
            ) {
        super(reference, domainEventName);
        this.addedToStockQuantity = addedToStockQuantity;
    }

    public static MaterialAddedToStockEventEntity of(MaterialStockChanged materialStockChanged) {
        return new MaterialAddedToStockEventEntity(
                materialStockChanged.getMaterialReference().getValue(),
                materialStockChanged.getClass().getSimpleName(),
                materialStockChanged.getMaterialInStock().getValue()
        );
    }

    @Override
    public MaterialStockChanged getDomainEvent() {
        return MaterialStockChanged.of(
                MaterialReference.of(getReference()),
                MaterialQuantity.of(addedToStockQuantity)
        );
    }

}
