package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate;

import lombok.Getter;
import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.DomainEventHandler;

public class BaseAggregateRoot<U extends DomainEventHandler,R> {

    @Getter
    private final R reference;
    private final U domainEventHandler;

    public BaseAggregateRoot(U domainEventHandler, R reference) {
        this.reference = reference;
        this.domainEventHandler = domainEventHandler;
    }

    protected <T extends BaseDomainEvent> void sendEvent(T domainEvent) {
        domainEventHandler.save(domainEvent);
    }

}
