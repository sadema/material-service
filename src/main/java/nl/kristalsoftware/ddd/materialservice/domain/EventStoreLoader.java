package nl.kristalsoftware.ddd.materialservice.domain;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;

public interface EventStoreLoader<T,U> {
    T loadAggregate(U reference) throws AggregateNotFoundException;

    T createAggregate();
}
