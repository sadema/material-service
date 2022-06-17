package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;

public interface MaterialAggregateRoot {
    void load(MaterialRegistered materialRegistered);
    void load(MaterialStockChanged materialStockChanged);
    void load(MaterialReserved materialReserved);
}
