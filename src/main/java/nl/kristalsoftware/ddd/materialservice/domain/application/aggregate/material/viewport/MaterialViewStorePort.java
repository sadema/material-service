package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;

import java.util.List;

public interface MaterialViewStorePort {

    List<MaterialView> getAllMaterial();
    MaterialView getMaterialByReference(MaterialReference materialReference);
    void save(List<MaterialDomainEvent> domainEventList, Material reference);
}
