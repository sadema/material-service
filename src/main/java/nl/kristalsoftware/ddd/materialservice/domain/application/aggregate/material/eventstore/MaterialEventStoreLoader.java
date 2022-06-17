package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.EventStoreLoader;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.MaterialDomainEventHandler;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MaterialEventStoreLoader implements EventStoreLoader<Material, MaterialReference> {

    private final MaterialDomainEventHandler materialDomainEventHandler;

    @Override
    public Material loadAggregate(MaterialReference reference) throws AggregateNotFoundException {
        Material material = Material.of(materialDomainEventHandler, reference);
        if (materialDomainEventHandler.getDomainEvents(material)) {
            return material;
        }
        throw new AggregateNotFoundException(String.format("MaterialReference: %s not found", reference.getValue()));
    }

    @Override
    public Material createAggregate() {
        return Material.of(materialDomainEventHandler, MaterialReference.of(UUID.randomUUID()));
    }

}
