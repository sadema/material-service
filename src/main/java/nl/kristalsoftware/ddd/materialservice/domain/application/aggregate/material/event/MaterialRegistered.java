package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.domain.EventStreamProvider;
import nl.kristalsoftware.ddd.materialservice.domain.ViewStoreProvider;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.AggregateProvider;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.MaterialEventStorePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstream.MaterialEventStreamPort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewStorePort;

@Slf4j
@Getter
@RequiredArgsConstructor(staticName = "of")
public class MaterialRegistered implements MaterialDomainEvent {

    private final MaterialReference materialReference;
    private final MaterialDescription materialDescription;
    private final MaterialQuantity materialInStock;

    @Override
    public void save(MaterialEventStorePort materialEventStorePort) {
        materialEventStorePort.save(this);
    }

    @Override
    public void save(MaterialViewStorePort materialViewStorePort) {
        materialViewStorePort.save(this);
    }

    @Override
    public void save(ViewStoreProvider viewStoreProvider) {
        viewStoreProvider.getViewStoreHandler(this).save(this);
    }

    @Override
    public void load(AggregateProvider aggregateProvider) {
        aggregateProvider.loadEvent(this);
    }

    @Override
    public void loadEventForAggregate(Material material) {
        material.load(this);
    }

    @Override
    public void produceEvent(MaterialEventStreamPort materialEventStreamPort) {

    }

    @Override
    public void produceEvent(EventStreamProvider eventStreamProvider) {

    }

}
