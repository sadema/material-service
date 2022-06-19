package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.AddMaterialToStock;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.RegisterMaterial;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.ReserveMaterialForTicket;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.CheckReturnValue;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MaterialTest {

    private static final String DESCRIPTION = "Schroefje";
    private static final Integer CURRENTLY_IN_STOCK_15 = 15;
    private static final Integer CURRENTLY_IN_STOCK_8 = 8;
    private static final Integer ADD_TO_STOCK_15 = 15;
    private static final Integer ADD_TO_STOCK_8 = 8;
    private static final Integer RESERVE_QUANTITY_5 = 5;
    private static final Integer RESERVE_QUANTITY_20 = 20;

    @Test
    void testHandleRegisterMaterialCommand() {
        tester()
                .withRegisterMaterialCommand(RegisterMaterial.of(
                        MaterialDescription.of(DESCRIPTION),
                        MaterialQuantity.of(ADD_TO_STOCK_15)
                ))
                .testHandleRegisterMaterialCommand()
                .shouldSendMaterialRegisteredEvent();

    }

    @Test
    void testHandleAddMaterialToStockCommand() {
        tester()
                .withAddMaterialToStockCommand(AddMaterialToStock.of(
                        MaterialQuantity.of(ADD_TO_STOCK_8)
                ))
                .testHandleAddMaterialToStockCommand()
                .shouldSendMaterialStockChangedEvent();

    }

    @Test
    void testHandleReserveMaterialForTicketCommand() {
        tester()
                .withReserveMaterialForTicketCommand(ReserveMaterialForTicket.of(
                        MaterialQuantity.of(RESERVE_QUANTITY_5),
                        TicketReference.of(UUID.randomUUID())
                ))
                .testHandleReserveMaterialForTicketCommand(CURRENTLY_IN_STOCK_8)
                .shouldSendMaterialReservedEvent();
    }

    @Test
    void testHandleReserveMaterialForTicketCommandWithTooLittleInStock() {
        tester()
                .withReserveMaterialForTicketCommand(ReserveMaterialForTicket.of(
                        MaterialQuantity.of(RESERVE_QUANTITY_20),
                        TicketReference.of(UUID.randomUUID())
                ))
                .testHandleReserveMaterialForTicketCommand(CURRENTLY_IN_STOCK_8)
                .shouldNotSendMaterialReservedEvent();
    }

    private Tester tester() {
        return new Tester();
    }

    @CheckReturnValue
    private static class Tester {
        private final MaterialDomainEventHandler materialDomainEventHandler = mock(MaterialDomainEventHandler.class);
        private final MaterialReference materialReference = MaterialReference.of(UUID.randomUUID());
        private RegisterMaterial registerMaterial;
        private AddMaterialToStock addMaterialToStock;
        private ReserveMaterialForTicket reserveMaterialForTicket;

        Tester withRegisterMaterialCommand(RegisterMaterial registerMaterial) {
            this.registerMaterial = registerMaterial;
            return this;
        }
        Tester withAddMaterialToStockCommand(AddMaterialToStock addMaterialToStock) {
            this.addMaterialToStock = addMaterialToStock;
            return this;
        }

        Tester withReserveMaterialForTicketCommand(ReserveMaterialForTicket reserveMaterialForTicket) {
            this.reserveMaterialForTicket = reserveMaterialForTicket;
            return this;
        }

        Asserter testHandleRegisterMaterialCommand() {
            Material cut = Material.of(materialDomainEventHandler, materialReference);
            cut.handleCommand(registerMaterial);
            return new Asserter(materialDomainEventHandler);
        }

        Asserter testHandleAddMaterialToStockCommand() {
            Material cut = Material.of(materialDomainEventHandler, materialReference);
            cut.handleCommand(addMaterialToStock);
            return new Asserter(materialDomainEventHandler);
        }

        Asserter testHandleReserveMaterialForTicketCommand(Integer currentlyInStock) {
            Material cut = Material.of(materialDomainEventHandler, materialReference);
            cut.load(MaterialRegistered.of(
                    materialReference,
                    MaterialDescription.of(DESCRIPTION),
                    MaterialQuantity.of(currentlyInStock)
            ));
            cut.handleCommand(reserveMaterialForTicket);
            return new Asserter(materialDomainEventHandler);
        }

    }

    private static class Asserter {
        private final MaterialDomainEventHandler materialDomainEventHandler;
        ArgumentCaptor<MaterialRegistered> materialRegisteredEventCaptor = ArgumentCaptor.forClass(MaterialRegistered.class);
        ArgumentCaptor<MaterialStockChanged> materialStockChangedEventCaptor = ArgumentCaptor.forClass(MaterialStockChanged.class);
        ArgumentCaptor<MaterialReserved> materialReservedEventCaptor = ArgumentCaptor.forClass(MaterialReserved.class);

        public Asserter(MaterialDomainEventHandler materialDomainEventHandler) {
            this.materialDomainEventHandler = materialDomainEventHandler;
        }

        Asserter shouldSendMaterialRegisteredEvent() {
            verify(materialDomainEventHandler, times(1)).save(materialRegisteredEventCaptor.capture());
            MaterialRegistered actual = materialRegisteredEventCaptor.getValue();
            assertEquals(DESCRIPTION, actual.getMaterialDescription().getValue());
            assertEquals(ADD_TO_STOCK_15, actual.getMaterialInStock().getValue());
            return this;
        }

        Asserter shouldSendMaterialStockChangedEvent() {
            verify(materialDomainEventHandler, times(1)).save(materialStockChangedEventCaptor.capture());
            MaterialStockChanged actual = materialStockChangedEventCaptor.getValue();
            assertEquals(ADD_TO_STOCK_8, actual.getMaterialInStock().getValue());
            return this;
        }

        Asserter shouldSendMaterialReservedEvent() {
            verify(materialDomainEventHandler, times(1)).save(materialReservedEventCaptor.capture());
            MaterialReserved actual = materialReservedEventCaptor.getValue();
            assertEquals(RESERVE_QUANTITY_5, actual.getReservedQuantity().getValue());
            return this;
        }

        public void shouldNotSendMaterialReservedEvent() {
            verify(materialDomainEventHandler, times(0)).save(materialReservedEventCaptor.capture());
        }
    }

}
