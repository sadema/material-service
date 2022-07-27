package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.domain.EventStreamProvider;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.AggregateEventsLoaderProvider;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.MaterialAggregateEventsLoader;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventPersistencePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstream.MaterialEventStreamPort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialDocumentPersistencePort;

@Slf4j
@Getter
@RequiredArgsConstructor(staticName = "of")
public class MaterialRegistered extends MaterialDomainEvent {

    private final MaterialReference materialReference;
    private final MaterialDescription materialDescription;
    private final MaterialQuantity materialInStock;

    @Override
    public void handleEvent(MaterialDocumentPersistencePort materialDocumentPersistencePort) {
        materialDocumentPersistencePort.handleEvent(this);
    }

    @Override
    public void handleEvent(MaterialEventPersistencePort materialEventPersistencePort) {
        materialEventPersistencePort.save(this);
    }

    @Override
    public void load(AggregateEventsLoaderProvider aggregateEventsLoaderProvider) {
        aggregateEventsLoaderProvider.loadEvent(this);
    }

    @Override
    public void loadEventForAggregate(MaterialAggregateEventsLoader material) {
        material.load(this);
    }

    @Override
    public void produceEvent(MaterialEventStreamPort materialEventStreamPort) {

    }

    @Override
    public void produceEvent(EventStreamProvider eventStreamProvider) {

    }

}
