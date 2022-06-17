package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.DomainEventHandler;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventStorePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewStorePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class MaterialDomainEventHandler implements DomainEventHandler<Material, MaterialDomainEvent> {

    private final MaterialEventStorePort materialEventStorePort;

    private final MaterialViewStorePort materialViewStorePort;

    @Transactional
    @Override
    public <T extends MaterialDomainEvent> void save(T domainEvent) {
        domainEvent.save(materialEventStorePort);
        domainEvent.save(materialViewStorePort);
    }

    @Override
    public Boolean getDomainEvents(Material aggregate) {
        return materialEventStorePort.getDomainEvents(aggregate);
    }
}
