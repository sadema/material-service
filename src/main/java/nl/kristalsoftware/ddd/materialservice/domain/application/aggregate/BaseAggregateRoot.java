package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate;

import lombok.Getter;
import nl.kristalsoftware.ddd.materialservice.domain.DomainEventHandler;

public abstract class BaseAggregateRoot<U extends DomainEventHandler,R> {

    @Getter
    private final R reference;
    protected final U domainEventHandler;
    @Getter
    public final Long version;
    @Getter
    public final boolean existingAggregate;

    protected BaseAggregateRoot(U domainEventHandler, R reference) {
        this.reference = reference;
        this.domainEventHandler = domainEventHandler;
        this.version = 0L;
        this.existingAggregate = false;
    }

    protected BaseAggregateRoot(U domainEventHandler, R reference, Long version) {
        this.reference = reference;
        this.domainEventHandler = domainEventHandler;
        this.version = version;
        this.existingAggregate = true;
    }

    public abstract void handleEvents();

}
