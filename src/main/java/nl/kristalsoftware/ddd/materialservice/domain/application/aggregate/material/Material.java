package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material;

import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.domain.base.annotations.AggregateRoot;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.BaseAggregateRoot;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.AddMaterialToStock;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.RegisterMaterial;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.ReserveMaterialForTicket;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.RetourMaterialForTicket;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.UseMaterialForTicket;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentBack;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@AggregateRoot
public class Material extends BaseAggregateRoot<MaterialDomainEventHandler, MaterialReference> implements MaterialAggregateRoot {

    private MaterialDomainEntity materialRootEntity = new MaterialDomainEntity();

    private Material(MaterialDomainEventHandler materialDomainEventHandler, MaterialReference materialReference) {
        super(materialDomainEventHandler, materialReference);
    }

    public static Material of(MaterialDomainEventHandler materialDomainEventHandler, MaterialReference materialReference) {
        return new Material(materialDomainEventHandler, materialReference);
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
        MaterialQuantity currentlyReserved = getCurrentReservedMaterialByTicket(ticketReference);
        MaterialQuantity newReserved = MaterialQuantity.of(currentlyReserved.add(materialReserved.getReservedQuantity()).getValue());
        materialRootEntity.getMaterialReservedByTicket().put(ticketReference, newReserved);
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

    public void load(MaterialSentBack materialSentBack) {
        MaterialQuantity currentlySentBack = materialRootEntity.getMaterialSentBack();
        materialRootEntity.setMaterialSentBack(currentlySentBack.add(materialSentBack.getSentBackQuantity()));
    }

    public void handleCommand(RegisterMaterial registerMaterial) {
        log.info("====> Register {} with a quantity of {}", registerMaterial.getMaterialDescription(), registerMaterial.getMaterialQuantity().getValue());
        this.sendEvent(MaterialRegistered.of(
                getReference(),
                registerMaterial.getMaterialDescription(),
                registerMaterial.getMaterialQuantity()
        ));
    }

    public void handleCommand(AddMaterialToStock addMaterialToStock) {
        log.info("====> Change stock with quantity {}", addMaterialToStock.getMaterialQuantity().getValue());
        this.sendEvent(MaterialStockChanged.of(
                getReference(),
                addMaterialToStock.getMaterialQuantity()
        ));
    }

    public Boolean handleCommand(ReserveMaterialForTicket reserveMaterialForTicket) {
        log.info("====> Reserve material for ticket with quantity {}", reserveMaterialForTicket.getReservedQuantity());
        MaterialQuantity currentlyInStock = materialRootEntity.getMaterialInStock();
        MaterialQuantity toReserveForTicket = reserveMaterialForTicket.getReservedQuantity();
        if (isEnoughMaterialInStockToReserve(currentlyInStock, toReserveForTicket)) {
            this.sendEvent(MaterialReserved.of(
                    getReference(),
                    toReserveForTicket,
                    reserveMaterialForTicket.getTicketReference()
            ));
            return true;
        }
        return false;
    }

    private boolean isEnoughMaterialInStockToReserve(MaterialQuantity currentlyInStock, MaterialQuantity toReserveForTicket) {
        return currentlyInStock.greaterThan(toReserveForTicket);
    }

    private boolean isEnoughMaterialInStockToUse(MaterialQuantity currentlyInStock, MaterialQuantity toUseForTicket) {
        return currentlyInStock.greaterThan(toUseForTicket);
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
            this.sendEvent(MaterialUsed.of(
                    getReference(),
                    toUseForTicket,
                    useMaterialForTicket.getTicketReference()
            ));
            this.sendEvent(MaterialStockChanged.of(
                    getReference(),
                    toUseForTicket.inverse()
            ));
            if (currentlyReserved.greaterThan(0)) {
                if (toUseForTicket.greaterThan(currentlyReserved)) {
                    this.sendEvent(MaterialReserved.of(
                            getReference(),
                            currentlyReserved.inverse(),
                            useMaterialForTicket.getTicketReference()
                    ));
                } else {
                    this.sendEvent(MaterialReserved.of(
                            getReference(),
                            toUseForTicket.inverse(),
                            useMaterialForTicket.getTicketReference()
                    ));
                }
            }
            return true;
        }
        return false;
    }

    @Transactional
    public void handleCommand(RetourMaterialForTicket retourMaterialForTicket) {
        this.sendEvent(MaterialSentBack.of(
                getReference(),
                retourMaterialForTicket.getRetourQuantity(),
                retourMaterialForTicket.getTicketReference()
        ));
        this.sendEvent(MaterialStockChanged.of(
                getReference(),
                retourMaterialForTicket.getRetourQuantity()
        ));
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
