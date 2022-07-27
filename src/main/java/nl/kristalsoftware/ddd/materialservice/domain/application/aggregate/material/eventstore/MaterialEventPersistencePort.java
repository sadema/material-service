package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentRetour;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;

import java.util.List;

public interface MaterialEventPersistencePort {
    void saveEvents(List<MaterialDomainEvent> eventList);

    void save(MaterialRegistered materialRegistered);

    void save(MaterialReserved materialReserved);

    void save(MaterialSentRetour materialSentRetour);

    void save(MaterialStockChanged materialStockChanged);

    void save(MaterialUsed materialUsed);
}
