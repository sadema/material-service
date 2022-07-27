package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentRetour;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;

import java.util.List;

public interface MaterialDocumentPersistencePort {
    void handleEvent(MaterialRegistered materialRegistered);
    void handleEvent(MaterialStockChanged materialStockChanged);
    void handleEvent(MaterialReserved materialReserved);
    void handleEvent(MaterialUsed materialUsed);
    void handleEvent(MaterialSentRetour materialSentRetour);

    void updateDocument(List<MaterialDomainEvent> domainEventList);
}
