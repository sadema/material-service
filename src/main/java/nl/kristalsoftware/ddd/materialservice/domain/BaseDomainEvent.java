package nl.kristalsoftware.ddd.materialservice.domain;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.AggregateProvider;

public interface BaseDomainEvent {

    void save(ViewStoreProvider viewStoreProvider);

    void load(AggregateProvider aggregateProvider);

    void produceEvent(EventStreamProvider eventStreamProvider);

}
