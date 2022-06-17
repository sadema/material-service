package nl.kristalsoftware.ddd.materialservice.domain.application;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.ViewStoreProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationService {

    private final ApplicationEventStorePort applicationEventStorePort;
    private final ViewStoreProvider viewStoreProvider;

    public void replayDomainEvents() {
        List<? extends BaseDomainEvent> domainEvents = applicationEventStorePort.getDomainEvents();
        domainEvents.stream()
                .forEach(it -> it.save(viewStoreProvider));
    }

    public void rebuildViewStore() {
        applicationEventStorePort.rebuildViewStore(viewStoreProvider);
    }

}
