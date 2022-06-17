package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentBack;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;

public interface MaterialEventStorePort {
    void save(MaterialRegistered materialRegistered);
    void save(MaterialStockChanged materialStockChanged);
    void save(MaterialReserved materialReserved);
    void save(MaterialUsed materialUsed);
    Boolean getDomainEvents(Material aggregate);

    void save(MaterialSentBack materialSentBack);
}
