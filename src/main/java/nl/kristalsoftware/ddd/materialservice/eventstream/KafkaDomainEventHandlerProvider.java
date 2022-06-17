package nl.kristalsoftware.ddd.materialservice.eventstream;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KafkaDomainEventHandlerProvider<T,V> {
    protected Map<T, KafkaDomainEventHandler<T,V>> eventMessageMap;

    public KafkaDomainEventHandlerProvider(List<KafkaDomainEventHandler<T,V>> kafkaDomainEventHandlers) {
        this.eventMessageMap = kafkaDomainEventHandlers.stream()
                .collect(Collectors.toMap((it) -> it.appliesTo(), it -> it));
    }

    public KafkaDomainEventHandler<T,V> getEventMessageHandler(String eventId) {
        return this.eventMessageMap.get(eventId);
    }
}
