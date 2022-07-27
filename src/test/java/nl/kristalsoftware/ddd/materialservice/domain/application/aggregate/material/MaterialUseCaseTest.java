package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.MaterialCommandService;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialView;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewService;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.TicketMaterialView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@SpringBootTest
public class MaterialUseCaseTest {

    private static final String MATERIAL_DESCRIPTION_1 = "Schroefje";

    private static final TicketReference TICKET_REFERENCE_1 = TicketReference.of(UUID.randomUUID());
    private static final TicketReference TICKET_REFERENCE_2 = TicketReference.of(UUID.randomUUID());

    @Autowired
    private MaterialCommandService materialCommandService;

    @Autowired
    private MaterialViewService materialViewService;

    @Test
    void testCreateMaterial() {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(15);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(0);

    }

    @Test
    void testAddMaterialToStock() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.addMaterialToStock(
                materialReference,
                MaterialQuantity.of(8));
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(23);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(0);
    }

    @Test
    void testReserveMaterialForTicket() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.reserveMaterialForTicket(
                materialReference,
                MaterialQuantity.of(3),
                TICKET_REFERENCE_1
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(15);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(1);
        assertThat(materialView.getMaterialByTicket())
                .contains(
                        entry(TICKET_REFERENCE_1, TicketMaterialView.of(
                                        MaterialQuantity.of(3),
                                        MaterialQuantity.of(0)
                                )
                        )
                );
    }

    @Test
    void testReserveTooMuchMaterialForTicket() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.reserveMaterialForTicket(
                materialReference,
                MaterialQuantity.of(20),
                TICKET_REFERENCE_1
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(15);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(0);
    }

    @Test
    void testUseMaterialForTicket() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.useMaterialForTicket(
                materialReference,
                MaterialQuantity.of(3),
                TICKET_REFERENCE_1
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(12);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(1);
        assertThat(materialView.getMaterialByTicket())
                .contains(
                        entry(TICKET_REFERENCE_1, TicketMaterialView.of(
                                        MaterialQuantity.of(0),
                                        MaterialQuantity.of(3)
                                )
                        )
                );
    }

    @Test
    void testSendMaterialRetourForTicket() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.useMaterialForTicket(
                materialReference,
                MaterialQuantity.of(8),
                TICKET_REFERENCE_1
        );
        materialCommandService.sendMaterialRetourForTicket(
                materialReference,
                MaterialQuantity.of(4),
                TICKET_REFERENCE_1
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(11);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(1);
        assertThat(materialView.getMaterialByTicket())
                .contains(
                        entry(TICKET_REFERENCE_1, TicketMaterialView.of(
                                        MaterialQuantity.of(0),
                                        MaterialQuantity.of(4)
                                )
                        )
                );
    }

    @Test
    void testReserveAndUseMaterialForTicket() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.reserveMaterialForTicket(
                materialReference,
                MaterialQuantity.of(3),
                TICKET_REFERENCE_1
        );
        materialCommandService.useMaterialForTicket(
                materialReference,
                MaterialQuantity.of(2),
                TICKET_REFERENCE_1
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(13);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(1);
        assertThat(materialView.getMaterialByTicket())
                .contains(
                        entry(TICKET_REFERENCE_1, TicketMaterialView.of(
                                        MaterialQuantity.of(1),
                                        MaterialQuantity.of(2)
                                )
                        )
                );
    }

    @Test
    void testReserveAndUseMaterialForTwoTickets() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.reserveMaterialForTicket(
                materialReference,
                MaterialQuantity.of(3),
                TICKET_REFERENCE_1
        );
        materialCommandService.useMaterialForTicket(
                materialReference,
                MaterialQuantity.of(2),
                TICKET_REFERENCE_1
        );
        materialCommandService.reserveMaterialForTicket(
                materialReference,
                MaterialQuantity.of(5),
                TICKET_REFERENCE_2
        );
        materialCommandService.useMaterialForTicket(
                materialReference,
                MaterialQuantity.of(7),
                TICKET_REFERENCE_2
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(13);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(2);
        assertThat(materialView.getMaterialByTicket())
                .contains(
                        entry(TICKET_REFERENCE_1, TicketMaterialView.of(
                                        MaterialQuantity.of(1),
                                        MaterialQuantity.of(2)
                                )
                        ),
                        entry(TICKET_REFERENCE_2, TicketMaterialView.of(
                                        MaterialQuantity.of(5),
                                        MaterialQuantity.of(0)
                                )
                        )
                );
    }

    @Test
    void testReserveMoreMaterialForTicketThanInStock() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.reserveMaterialForTicket(
                materialReference,
                MaterialQuantity.of(20),
                TICKET_REFERENCE_1
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(15);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(0);
    }

    @Test
    void testUseMoreMaterialForTicketThanInStock() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.useMaterialForTicket(
                materialReference,
                MaterialQuantity.of(20),
                TICKET_REFERENCE_1
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(15);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(0);
    }

    @Test
    void testUseMaterialForTicketWithTooManyReserved() throws AggregateNotFoundException {
        // Arrange
        MaterialReference materialReference = materialCommandService.registerNewMaterialType(
                MaterialDescription.of(MATERIAL_DESCRIPTION_1),
                MaterialQuantity.of(15)
        );
        materialCommandService.reserveMaterialForTicket(
                materialReference,
                MaterialQuantity.of(10),
                TICKET_REFERENCE_1
        );
        materialCommandService.useMaterialForTicket(
                materialReference,
                MaterialQuantity.of(2),
                TICKET_REFERENCE_2
        );
        materialCommandService.useMaterialForTicket(
                materialReference,
                MaterialQuantity.of(12),
                TICKET_REFERENCE_2
        );
        // Act
        MaterialView materialView = materialViewService.getMaterialByReference(materialReference);
        // Assert
        assertThat(MATERIAL_DESCRIPTION_1).isEqualTo(materialView.getMaterialDescription().getValue());
        assertThat(materialView.getMaterialItemsInStock().getValue()).isEqualTo(13);
        assertThat(materialView.getMaterialByTicket().size()).isEqualTo(2);
        assertThat(materialView.getMaterialByTicket())
                .contains(
                        entry(TICKET_REFERENCE_1, TicketMaterialView.of(
                                        MaterialQuantity.of(10),
                                        MaterialQuantity.of(0)
                                )
                        ),
                        entry(TICKET_REFERENCE_2, TicketMaterialView.of(
                                        MaterialQuantity.of(0),
                                        MaterialQuantity.of(2)
                                )
                        )
                );
    }

}
