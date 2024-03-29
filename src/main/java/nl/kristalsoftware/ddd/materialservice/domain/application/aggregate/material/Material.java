package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.domain.base.annotations.AggregateRoot;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.BaseAggregateRoot;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.AddMaterialToStock;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.RegisterMaterial;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.ReserveMaterialForTicket;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.SendMaterialRetourForTicket;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.UseMaterialForTicket;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentRetour;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@AggregateRoot
public class Material extends BaseAggregateRoot<MaterialDomainEventHandler, MaterialReference> implements MaterialAggregateEventsLoader {

    private final List<MaterialDomainEvent> eventList = new ArrayList<>();

    private MaterialDomainEntity materialRootEntity = new MaterialDomainEntity();

    private Material(MaterialDomainEventHandler materialDomainEventHandler, MaterialReference materialReference) {
        super(materialDomainEventHandler, materialReference);
    }

    private Material(MaterialDomainEventHandler materialDomainEventHandler, MaterialReference materialReference, Long version) {
        super(materialDomainEventHandler, materialReference, version);
    }

    @Override
    public void handleEvents() {
        domainEventHandler.handleEvents(eventList, this);
    }

    public static Material of(MaterialDomainEventHandler materialDomainEventHandler, MaterialReference materialReference) {
        return new Material(materialDomainEventHandler, materialReference);
    }

    public static Material of(MaterialDomainEventHandler materialDomainEventHandler, MaterialReference materialReference, Long version) {
        return new Material(materialDomainEventHandler, materialReference, version);
    }

    @Override
    public void load(MaterialRegistered materialRegistered) {
        materialRootEntity.setMaterialDescription(materialRegistered.getMaterialDescription());
        materialRootEntity.setMaterialInStock(materialRegistered.getMaterialInStock());
    }

    @Override
    public void load(MaterialStockChanged materialStockChanged) {
        MaterialQuantity currentlyInStock = materialRootEntity.getMaterialInStock();
        MaterialQuantity addedToStock = materialStockChanged.getMaterialInStock();
        materialRootEntity.setMaterialInStock(currentlyInStock.add(addedToStock));
    }

    @Override
    public void load(MaterialReserved materialReserved) {
        TicketReference ticketReference = materialReserved.getTicketReference();
        MaterialQuantity currentlyReservedByTicket = getCurrentReservedMaterialByTicket(ticketReference);
        MaterialQuantity newReserved = MaterialQuantity.of(currentlyReservedByTicket.add(materialReserved.getReservedQuantity()).getValue());
        materialRootEntity.getMaterialReservedByTicket().put(ticketReference, newReserved);
        MaterialQuantity currentlyReserved = materialRootEntity.getMaterialReserved();
        materialRootEntity.setMaterialReserved(currentlyReserved.add(materialReserved.getReservedQuantity()));
    }

    private MaterialQuantity getCurrentReservedMaterialByTicket(TicketReference ticketReference) {
        Map<TicketReference, MaterialQuantity> reservedByTicket = materialRootEntity.getMaterialReservedByTicket();
        return reservedByTicket.computeIfAbsent(ticketReference, key -> MaterialQuantity.of(0));
    }

    public void load(MaterialUsed materialUsed) {
        MaterialQuantity currentlyUsed = materialRootEntity.getMaterialUsed();
        MaterialQuantity addedToUsed = materialUsed.getUsedQuantity();
        materialRootEntity.setMaterialUsed(currentlyUsed.add(addedToUsed));
    }

    public void load(MaterialSentRetour materialSentRetour) {
        MaterialQuantity currentlySentBack = materialRootEntity.getMaterialSentBack();
        materialRootEntity.setMaterialSentBack(currentlySentBack.add(materialSentRetour.getSentRetourQuantity()));
    }

    public void handleCommand(RegisterMaterial registerMaterial) {
        log.info("====> Register {} with a quantity of {}", registerMaterial.getMaterialDescription(), registerMaterial.getMaterialQuantity().getValue());
        eventList.add(MaterialRegistered.of(
                getReference(),
                registerMaterial.getMaterialDescription(),
                registerMaterial.getMaterialQuantity()
        ));
        handleEvents();
    }

    public void handleCommand(AddMaterialToStock addMaterialToStock) {
        log.info("====> Change stock with quantity {}", addMaterialToStock.getMaterialQuantity().getValue());
        eventList.add(MaterialStockChanged.of(
                getReference(),
                addMaterialToStock.getMaterialQuantity()
        ));
        handleEvents();
    }

    public Boolean handleCommand(ReserveMaterialForTicket reserveMaterialForTicket) {
        log.info("====> Reserve material for ticket with quantity {}", reserveMaterialForTicket.getReservedQuantity());
        MaterialQuantity currentlyInStock = materialRootEntity.getMaterialInStock();
        MaterialQuantity toReserveForTicket = reserveMaterialForTicket.getReservedQuantity();
        if (isEnoughMaterialInStockToReserve(currentlyInStock, toReserveForTicket)) {
            eventList.add(MaterialReserved.of(
                    getReference(),
                    toReserveForTicket,
                    reserveMaterialForTicket.getTicketReference()
            ));
            handleEvents();
            return true;
        }
        return false;
    }

    private boolean isEnoughMaterialInStockToReserve(MaterialQuantity currentlyInStock, MaterialQuantity toReserveForTicket) {
        MaterialQuantity maxToReserve = currentlyInStock.substract(materialRootEntity.getMaterialReserved());
        return maxToReserve.greaterThan(toReserveForTicket);
    }

    private boolean isEnoughMaterialInStockToUse(MaterialQuantity currentlyInStock, MaterialQuantity toUseForTicket) {
        MaterialQuantity maxToUse = currentlyInStock.substract(materialRootEntity.getMaterialReserved());
        return maxToUse.greaterThan(toUseForTicket);
    }

    @Transactional
    public Boolean handleCommand(UseMaterialForTicket useMaterialForTicket) {
        log.info("====> Use material for ticket with quantity {}", useMaterialForTicket.getUsedQuantity().getValue());
        MaterialQuantity currentlyInStock = materialRootEntity.getMaterialInStock();
        MaterialQuantity currentlyReserved = getCurrentReservedMaterialByTicket(useMaterialForTicket.getTicketReference());
        MaterialQuantity currentlyUsed = materialRootEntity.getMaterialUsed();
        log.info("currentlyInStock: {}, currentlyReserved: {}, currentlyUsed: {}", currentlyInStock, currentlyReserved, currentlyUsed);

        MaterialQuantity toUseForTicket = useMaterialForTicket.getUsedQuantity();
        if (isEnoughMaterialInStockToUse(currentlyInStock, toUseForTicket)) {
            eventList.add(MaterialUsed.of(
                    getReference(),
                    toUseForTicket,
                    useMaterialForTicket.getTicketReference()
            ));
            eventList.add(MaterialStockChanged.of(
                    getReference(),
                    toUseForTicket.inverse()
            ));
            if (currentlyReserved.greaterThan(0)) {
                if (toUseForTicket.greaterThan(currentlyReserved)) {
                    eventList.add(MaterialReserved.of(
                            getReference(),
                            currentlyReserved.inverse(),
                            useMaterialForTicket.getTicketReference()
                    ));
                } else {
                    eventList.add(MaterialReserved.of(
                            getReference(),
                            toUseForTicket.inverse(),
                            useMaterialForTicket.getTicketReference()
                    ));
                }
            }
            handleEvents();
            return true;
        }
        return false;
    }

    @Transactional
    public void handleCommand(SendMaterialRetourForTicket sendMaterialRetourForTicket) {
        eventList.add(MaterialSentRetour.of(
                getReference(),
                sendMaterialRetourForTicket.getRetourQuantity(),
                sendMaterialRetourForTicket.getTicketReference()
        ));
        handleEvents();
    }

    public String toString() {
        StringBuffer message = new StringBuffer();
        message.append(String.format("description: %s, in stock: %s, used: %s",
                materialRootEntity.getMaterialDescription().getValue(),
                materialRootEntity.getMaterialInStock().getValue(),
                materialRootEntity.getMaterialUsed().getValue()
        ));
        message.append(" reserved:");
        materialRootEntity.getMaterialReservedByTicket().values().stream()
                .forEach(it -> {
                    message.append(" " + it.getValue());
                });
        return message.toString();
    }

}
