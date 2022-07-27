package nl.kristalsoftware.ddd.materialservice.domain.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApplicationService {

    private final ApplicationEventStorePort applicationEventStorePort;

//    public void replayDomainEvents() {
//        List<? extends BaseDomainEvent> domainEvents = applicationEventStorePort.getDomainEvents();
//        domainEvents.stream()
//                .forEach(it -> {
//                    it.setVersion();
//                    it.save(viewStoreProvider);
//                });
//    }

    @Deprecated
    public void rebuildViewStore() {
//        applicationEventStorePort.rebuildViewStore(viewStoreProvider);
    }

}
