package nl.kristalsoftware.ddd.materialservice.eventstore;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.ViewStoreProvider;
import nl.kristalsoftware.ddd.materialservice.domain.application.ApplicationEventStorePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component
public class ApplicationEventStoreAdapter implements ApplicationEventStorePort {

    private final UUIDEventStoreRepository eventStoreRepository;

    @Override
    public List<? extends BaseDomainEvent> getDomainEvents() {
        return StreamSupport.stream(eventStoreRepository.findAll().spliterator(), false)
                .map(it -> it.getDomainEvent())
                .collect(Collectors.toList());

    }

    @Override
    public void rebuildViewStore(ViewStoreProvider viewStoreProvider) {
        StreamSupport.stream(eventStoreRepository.findAll().spliterator(), false)
                .map(it -> it.getDomainEvent())
                .forEach(it -> it.save(viewStoreProvider));
    }

}
