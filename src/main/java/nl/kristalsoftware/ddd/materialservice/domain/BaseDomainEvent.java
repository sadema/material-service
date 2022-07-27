package nl.kristalsoftware.ddd.materialservice.domain;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.AggregateEventsLoaderProvider;

public interface BaseDomainEvent {
    void load(AggregateEventsLoaderProvider aggregateEventsLoaderProvider);

    void produceEvent(EventStreamProvider eventStreamProvider);

}
