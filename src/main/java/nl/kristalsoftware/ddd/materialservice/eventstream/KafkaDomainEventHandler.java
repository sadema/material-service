package nl.kristalsoftware.ddd.materialservice.eventstream;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;

public interface KafkaDomainEventHandler<T,V> {

    T appliesTo();

    void save(V message) throws AggregateNotFoundException;

}
