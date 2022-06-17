package nl.kristalsoftware.ddd.materialservice.domain.application;

import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.ViewStoreProvider;

import java.util.List;

public interface ApplicationEventStorePort {

    List<? extends BaseDomainEvent> getDomainEvents();

    void rebuildViewStore(ViewStoreProvider viewStoreProvider);
}
