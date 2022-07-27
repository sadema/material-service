package nl.kristalsoftware.ddd.materialservice.viewstore.material;

import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentRetour;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialDocumentPersistencePort;
import nl.kristalsoftware.ddd.materialservice.viewstore.material.document.MaterialDocument;
import nl.kristalsoftware.ddd.materialservice.viewstore.material.document.MaterialDocumentRepository;
import nl.kristalsoftware.ddd.materialservice.viewstore.material.document.MaterialDocumentTicketPart;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MaterialDocumentPersistenceAdapter implements MaterialDocumentPersistencePort {

    private final MaterialDocumentRepository materialDocumentRepository;
    private final Material material;
    private MaterialDocument materialDocument;

    public MaterialDocumentPersistenceAdapter(MaterialDocumentRepository materialDocumentRepository, Material material) {
        this.materialDocumentRepository = materialDocumentRepository;
        this.material = material;
    }

    public void updateDocument(List<MaterialDomainEvent> domainEventList) {
        materialDocument = getMaterialDocument();
        domainEventList.stream().forEach(it -> it.handleEvent(this));
        save();
    }

    private MaterialDocument getMaterialDocument() {
        if (material.isExistingAggregate()) {
            MaterialDocument materialDocument = materialDocumentRepository.findByReference(material.getReference().getValue())
                    .orElseThrow(() -> new MaterialNotFoundException(String.format("Material with reference %s not found", material.getReference().getValue())));
            materialDocument.setVersion(material.getVersion());
            return materialDocument;
        }
        return new MaterialDocument();
    }

    private void save() {
        if (materialDocument.getReference() != null) {
            materialDocumentRepository.save(materialDocument);
        }
        else {
            throw new IllegalStateException("Material has never been registered!");
        }
    }

    @Override
    public void handleEvent(MaterialRegistered materialRegistered) {
        materialDocument = MaterialDocument.of(
                materialRegistered.getMaterialReference().getValue(),
                materialRegistered.getMaterialDescription().getValue(),
                materialRegistered.getMaterialInStock().getValue()
        );
    }

    @Override
    public void handleEvent(MaterialStockChanged materialStockChanged) {
        Integer calculatedStock = materialDocument.getInStock() + materialStockChanged.getMaterialInStock().getValue();
        materialDocument.setInStock(calculatedStock);
    }

    @Override
    public void handleEvent(MaterialReserved materialReserved) {
        UUID ticketReference = materialReserved.getTicketReference().getValue();
        MaterialDocumentTicketPart ticketMaterial = getTicketMaterial(materialDocument, ticketReference);
        Integer calculatedReserved = ticketMaterial.getReserved() + materialReserved.getReservedQuantity().getValue();
        ticketMaterial.setReserved(calculatedReserved);
    }

    @Override
    public void handleEvent(MaterialUsed materialUsed) {
        UUID ticketReference = materialUsed.getTicketReference().getValue();
        MaterialDocumentTicketPart ticketMaterial = getTicketMaterial(materialDocument, ticketReference);
        Integer calculatedUsed = ticketMaterial.getUsed() + materialUsed.getUsedQuantity().getValue();
        ticketMaterial.setUsed(calculatedUsed);
    }

    @Override
    public void handleEvent(MaterialSentRetour materialSentRetour) {
        UUID ticketReference = materialSentRetour.getTicketReference().getValue();
        Integer inStock = materialDocument.getInStock();
        Integer sentRetour = materialSentRetour.getSentRetourQuantity().getValue();
        materialDocument.setInStock(inStock + sentRetour);
        MaterialDocumentTicketPart ticketMaterial = getTicketMaterial(materialDocument, ticketReference);
        Integer calculatedUsed = ticketMaterial.getUsed() - sentRetour;
        ticketMaterial.setUsed(calculatedUsed);
    }

    private MaterialDocumentTicketPart getTicketMaterial(MaterialDocument materialDocument, UUID ticketReference) {
        Map<UUID, MaterialDocumentTicketPart> materialByTicket = materialDocument.getMaterialByTicket();
        return materialByTicket.computeIfAbsent(ticketReference, key -> new MaterialDocumentTicketPart());
    }

}
