package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.domain.EventStoreLoader;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Validator;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MaterialCommandService {

    private final EventStoreLoader<Material, MaterialReference> eventStoreLoader;
    private final Validator validator;

    public MaterialReference registerNewMaterialType(
            MaterialDescription materialDescription,
            MaterialQuantity materialQuantity) {
        RegisterMaterial registerMaterial = RegisterMaterial.of(materialDescription, materialQuantity);
        validator.isValid(registerMaterial);
        Material material = eventStoreLoader.createAggregate();
        material.handleCommand(registerMaterial);
        printMaterial(material.getReference());
        return material.getReference();
    }

    public void addMaterialToStock(
            MaterialReference materialReference,
            MaterialQuantity quantity
    ) throws AggregateNotFoundException {
        Material material = eventStoreLoader.loadAggregate(materialReference);
        material.handleCommand(AddMaterialToStock.of(quantity));
        printMaterial(material.getReference());
    }

    public Boolean reserveMaterialForTicket(
            MaterialReference materialReference,
            MaterialQuantity reservedQuantity,
            TicketReference ticketReference
    ) throws AggregateNotFoundException {
        Material material = eventStoreLoader.loadAggregate(materialReference);
        if (material.handleCommand(
                ReserveMaterialForTicket.of(
                        reservedQuantity,
                        ticketReference
                ))
        ) {
            printMaterial(material.getReference());
            return true;
        }
        return false;
    }

    public Boolean useMaterialForTicket(
            MaterialReference materialReference,
            MaterialQuantity usedQuantity,
            TicketReference ticketReference) throws AggregateNotFoundException {
        Material material = eventStoreLoader.loadAggregate(materialReference);
        if (material.handleCommand(
                UseMaterialForTicket.of(
                        usedQuantity,
                        ticketReference
                ))
        ) {
            printMaterial(material.getReference());
            return true;
        }
        return false;
    }

    public void retourMaterialForTicket(
            MaterialReference materialReference,
            MaterialQuantity retourQuantity,
            TicketReference ticketReference) throws AggregateNotFoundException {
        Material material = eventStoreLoader.loadAggregate(materialReference);
        material.handleCommand(SendMaterialRetourForTicket.of(
                retourQuantity,
                ticketReference
        ));
        printMaterial(material.getReference());
    }

    private void printMaterial(MaterialReference materialReference) {
        try {
            Material material = eventStoreLoader.loadAggregate(materialReference);
            log.info(material.toString());
        } catch (AggregateNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
