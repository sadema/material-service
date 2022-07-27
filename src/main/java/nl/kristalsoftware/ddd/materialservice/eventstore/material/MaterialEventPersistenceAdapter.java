package nl.kristalsoftware.ddd.materialservice.eventstore.material;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentRetour;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventPersistencePort;
import nl.kristalsoftware.ddd.materialservice.eventstore.UUIDEventStoreRepository;

import java.util.List;

public class MaterialEventPersistenceAdapter implements MaterialEventPersistencePort {

    private final UUIDEventStoreRepository repository;

    public MaterialEventPersistenceAdapter(UUIDEventStoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveEvents(List<MaterialDomainEvent> eventList) {
        eventList.stream().forEach(it -> it.handleEvent(this));
    }

    @Override
    public void save(MaterialRegistered materialRegistered) {
        repository.save(MaterialRegisteredEventEntity.of(materialRegistered));
    }

    @Override
    public void save(MaterialStockChanged materialStockChanged) {
        repository.save(MaterialAddedToStockEventEntity.of(materialStockChanged));
    }

    @Override
    public void save(MaterialReserved materialReserved) {
        repository.save(MaterialReservedEventEntity.of(materialReserved));
    }

    @Override
    public void save(MaterialUsed materialUsed) {
        repository.save(MaterialUsedEventEntity.of(materialUsed));
    }

    @Override
    public void save(MaterialSentRetour materialSentRetour) {
        repository.save(MaterialSentRetourEventEntity.of(materialSentRetour));
    }

}
