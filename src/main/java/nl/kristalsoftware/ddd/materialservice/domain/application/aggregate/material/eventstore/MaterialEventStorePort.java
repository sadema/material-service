package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;

import java.util.List;

public interface MaterialEventStorePort {
    Boolean loadAllDomainEvents(Material aggregate);

    void save(List<MaterialDomainEvent> eventList);
}
