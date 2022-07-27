package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentRetour;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;

public interface MaterialAggregateEventsLoader {
    void load(MaterialRegistered materialRegistered);
    void load(MaterialStockChanged materialStockChanged);
    void load(MaterialReserved materialReserved);
    void load(MaterialSentRetour materialSentRetour);
    void load(MaterialUsed materialUsed);
}
