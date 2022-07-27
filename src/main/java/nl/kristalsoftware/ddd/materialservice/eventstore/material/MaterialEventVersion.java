package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDBaseEventEntity;
import org.springframework.data.annotation.Version;

import java.util.UUID;

@NoArgsConstructor
public abstract class MaterialEventVersion<T extends BaseDomainEvent> extends UUIDBaseEventEntity<T> {

    @Getter
    @Version
    private Long version;

    public MaterialEventVersion(UUID reference, String domainEventName) {
        super(reference, domainEventName);
    }
}
