package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.AggregateProvider;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentBack;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventStorePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDEventStoreRepository;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component
public class MaterialEventStoreAdapter implements MaterialEventStorePort {

    private final UUIDEventStoreRepository repository;

    @Override
    public void save(MaterialRegistered materialRegistered) {
        repository.save(MaterialRegisteredEventEntity.of(materialRegistered));
    }

    @Override
    public void save(MaterialStockChanged materialStockChanged) {
        repository.save(MaterialAddedToStockEventEntity.of(materialStockChanged));
    }

    @Override
    public void save(MaterialReserved materialReserved) {
        repository.save(MaterialReservedEventEntity.of(materialReserved));
    }

    @Override
    public void save(MaterialUsed materialUsed) {
        repository.save(MaterialUsedEventEntity.of(materialUsed));
    }

    @Override
    public void save(MaterialSentBack materialSentBack) {

    }

    @Override
    public Boolean getDomainEvents(Material aggregate) {
        Boolean aggregateExists = aggregateExists(aggregate.getReference());
        if (aggregateExists) {
            StreamSupport.stream(repository.findAllByReference(aggregate.getReference().getValue()).spliterator(), false)
                    .map(it -> it.getDomainEvent())
                    .forEach(it -> it.load(new AggregateProvider(aggregate)));
        }
        return aggregateExists;
    }

    private Boolean aggregateExists(MaterialReference reference) {
        if (repository.findFirstByReference(reference.getValue()).isPresent()) {
            return true;
        }
        return false;
    }

}
