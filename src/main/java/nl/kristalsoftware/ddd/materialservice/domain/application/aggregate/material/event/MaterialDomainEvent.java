package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event;

import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventStorePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstream.MaterialEventStreamPort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewStorePort;

public interface MaterialDomainEvent extends BaseDomainEvent {
    void save(MaterialEventStorePort materialEventStorePort);
    void save(MaterialViewStorePort materialViewStorePort);
    void loadEventForAggregate(Material material);

    void produceEvent(MaterialEventStreamPort materialEventStreamPort);
}
