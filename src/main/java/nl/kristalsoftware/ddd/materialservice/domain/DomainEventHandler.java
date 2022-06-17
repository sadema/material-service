package nl.kristalsoftware.ddd.materialservice.domain;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.BaseAggregateRoot;

public interface DomainEventHandler<U extends BaseAggregateRoot<?,?>,R> {
    <T extends R> void save(T domainEvent);

    Boolean getDomainEvents(U aggregate);
}
