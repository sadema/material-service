package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import nl.kristalsoftware.ddd.materialservice.domain.EventStoreLoader;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.AddMaterialToStock;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.MaterialCommandService;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialView;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class MaterialTransactionTest {

    private static final String MATERIAL_DESCRIPTION_1 = "Schroefje";

    @Autowired
    private MaterialCommandService materialCommandService;

    @Autowired
    private MaterialViewService materialViewService;

    @Autowired
    private EventStoreLoader<Material, MaterialReference> eventStoreLoader;

    @Test
    public void testMaterialVersionConflict() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        Material material_ts1 = eventStoreLoader.loadAggregate(materialReference);
        assertThat(material_ts1.getVersion()).isEqualTo(0);
        Material material_ts2 = eventStoreLoader.loadAggregate(materialReference);
        assertThat(material_ts2.getVersion()).isEqualTo(0);

        material_ts1.handleCommand(AddMaterialToStock.of(MaterialQuantity.of(10)));
        assertThatThrownBy(() -> {
            material_ts2.handleCommand(AddMaterialToStock.of(MaterialQuantity.of(75)));
        }).isInstanceOf(OptimisticLockingFailureException.class);
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(25);
    }
}
