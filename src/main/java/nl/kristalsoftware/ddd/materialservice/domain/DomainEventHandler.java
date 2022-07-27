package nl.kristalsoftware.ddd.materialservice.domain;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.BaseAggregateRoot;

import java.util.List;

public interface DomainEventHandler<U extends BaseAggregateRoot<?,?>,E> {
    Boolean getDomainEvents(U aggregate);

    void handleEvents(List<E> eventList, U aggregate);
}
