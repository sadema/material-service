package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.DomainEventHandler;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventStorePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewStorePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class MaterialDomainEventHandler implements DomainEventHandler<Material, MaterialDomainEvent> {

    private final MaterialEventStorePort materialEventStorePort;

    private final MaterialViewStorePort materialViewStorePort;

    @Override
    public Boolean getDomainEvents(Material aggregate) {
        Boolean allDomainEventsLoaded = materialEventStorePort.loadAllDomainEvents(aggregate);
        return allDomainEventsLoaded;
    }

    @Transactional
    @Override
    public void handleEvents(List<MaterialDomainEvent> eventList, Material aggregate) {
        materialEventStorePort.save(eventList);
        materialViewStorePort.save(eventList, aggregate);
    }

}
