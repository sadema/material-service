package nl.kristalsoftware.ddd.materialservice.domain;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewStorePort;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ViewStoreProvider {

    private final MaterialViewStorePort materialViewStorePort;

    public MaterialViewStorePort getViewStoreHandler(MaterialDomainEvent materialDomainEvent) {
        return materialViewStorePort;
    }
}
