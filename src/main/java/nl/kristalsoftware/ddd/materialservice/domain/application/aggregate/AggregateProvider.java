package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;

public class AggregateProvider {

    private Material material = null;

    public AggregateProvider(Material material) {
        this.material = material;
    }

    public void loadEvent(MaterialDomainEvent materialDomainEvent) {
        materialDomainEvent.loadEventForAggregate(material);
    }

}
