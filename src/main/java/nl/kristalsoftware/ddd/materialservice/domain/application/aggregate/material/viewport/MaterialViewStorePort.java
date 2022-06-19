package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentRetour;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;

import java.util.List;

public interface MaterialViewStorePort {

    void save(MaterialRegistered materialRegistered);
    void save(MaterialStockChanged materialStockChanged);
    void save(MaterialReserved materialReserved);
    void save(MaterialUsed materialUsed);
    void save(MaterialSentRetour materialSentRetour);
    List<MaterialView> getAllMaterial();
    MaterialView getMaterialByReference(MaterialReference materialReference);

}
