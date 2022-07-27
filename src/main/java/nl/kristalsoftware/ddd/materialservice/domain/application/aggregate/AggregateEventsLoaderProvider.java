package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.MaterialAggregateEventsLoader;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;

public class AggregateEventsLoaderProvider {

    private MaterialAggregateEventsLoader materialAggregateEventsLoader = null;

    public AggregateEventsLoaderProvider(MaterialAggregateEventsLoader materialAggregateEventsLoader) {
        this.materialAggregateEventsLoader = materialAggregateEventsLoader;
    }

    public void loadEvent(MaterialDomainEvent materialDomainEvent) {
        materialDomainEvent.loadEventForAggregate(materialAggregateEventsLoader);
    }

}
