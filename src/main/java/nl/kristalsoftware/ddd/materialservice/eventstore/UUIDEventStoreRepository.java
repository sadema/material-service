package nl.kristalsoftware.ddd.materialservice.eventstore;

import nl.kristalsoftware.ddd.materialservice.domain.BaseDomainEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UUIDEventStoreRepository extends CrudRepository<UUIDBaseEventEntity<? extends BaseDomainEvent>, Long> {

    Iterable<UUIDBaseEventEntity<? extends BaseDomainEvent>> findAllByReference(UUID value);

    Optional<UUIDBaseEventEntity<? extends BaseDomainEvent>> findFirstByReference(UUID value);

}
