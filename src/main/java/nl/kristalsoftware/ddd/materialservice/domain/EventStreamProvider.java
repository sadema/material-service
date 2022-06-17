package nl.kristalsoftware.ddd.materialservice.domain;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstream.MaterialEventStreamPort;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventStreamProvider {

    private final MaterialEventStreamPort materialEventStreamPort;

    public void getEventStreamHandler(MaterialDomainEvent materialDomainEvent) {
        materialDomainEvent.produceEvent(materialEventStreamPort);
    }

}
