package nl.kristalsoftware.ddd.materialservice.eventstore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public abstract class UUIDBaseEventEntity<T extends BaseDomainEvent> {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Type(type = "uuid-char")
    private UUID reference;

    private String domainEventName;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    private LocalDateTime creationDateTime;

    public UUIDBaseEventEntity(UUID reference, String domainEventName) {
        this.reference = reference;
        this.domainEventName = domainEventName;
    }

    public abstract T getDomainEvent();
}
