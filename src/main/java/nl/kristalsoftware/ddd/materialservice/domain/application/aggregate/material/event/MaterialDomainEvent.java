package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event;

import lombok.Getter;
import lombok.Setter;
import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.MaterialAggregateEventsLoader;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventPersistencePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstream.MaterialEventStreamPort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialDocumentPersistencePort;

public abstract class MaterialDomainEvent implements BaseDomainEvent {
    @Getter
    @Setter
    private Long version = 0L;
    public abstract void handleEvent(MaterialDocumentPersistencePort materialDocumentPersistencePort);
    public abstract void handleEvent(MaterialEventPersistencePort materialEventPersistencePort);
    public abstract void loadEventForAggregate(MaterialAggregateEventsLoader material);
    public abstract void produceEvent(MaterialEventStreamPort materialEventStreamPort);
    public abstract MaterialReference getMaterialReference();

}
