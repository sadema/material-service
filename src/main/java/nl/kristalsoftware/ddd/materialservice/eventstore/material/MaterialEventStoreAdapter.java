package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.AggregateEventsLoaderProvider;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventPersistencePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventStorePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDEventStoreRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component
public class MaterialEventStoreAdapter implements MaterialEventStorePort {

    private final UUIDEventStoreRepository repository;

    @Override
    public Boolean loadAllDomainEvents(Material aggregate) {
        Boolean aggregateExists = aggregateExists(aggregate.getReference());
        if (aggregateExists) {
            StreamSupport.stream(repository.findAllByReference(aggregate.getReference().getValue()).spliterator(), false)
                    .map(it -> it.getDomainEvent())
                    .forEach(it -> it.load(new AggregateEventsLoaderProvider(aggregate)));
        }
        return aggregateExists;
    }

    @Override
    public void save(List<MaterialDomainEvent> eventList) {
        MaterialEventPersistencePort materialEventPersistencePort = new MaterialEventPersistenceAdapter(repository);
        materialEventPersistencePort.saveEvents(eventList);
    }

    private Boolean aggregateExists(MaterialReference reference) {
        if (repository.findFirstByReference(reference.getValue()).isPresent()) {
            return true;
        }
        return false;
    }

}
