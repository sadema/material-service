package nl.kristalsoftware.ddd.materialservice.viewstore.material;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialRegistered;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialReserved;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialSentBack;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialStockChanged;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialUsed;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialView;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewStorePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.TicketMaterialView;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MaterialViewStoreAdapter implements MaterialViewStorePort {

    private final MaterialDocumentRepository materialDocumentRepository;

    @Override
    public void save(MaterialRegistered materialRegistered) {
        MaterialDocument materialDocument = MaterialDocument.of(
                materialRegistered.getMaterialReference().getValue(),
                materialRegistered.getMaterialDescription().getValue(),
                materialRegistered.getMaterialInStock().getValue()
        );
        materialDocumentRepository.save(materialDocument);
    }

    @Override
    public void save(MaterialStockChanged materialStockChanged) {
        MaterialDocument materialDocument = materialDocumentRepository.findByReference(materialStockChanged.getMaterialReference().getValue())
                .orElseThrow(() -> new MaterialNotFoundException(String.format("Material with reference %s not found", materialStockChanged.getMaterialReference().getValue())));
        Integer calculatedStock = materialDocument.getInStock() + materialStockChanged.getMaterialInStock().getValue();
        materialDocument.setInStock(calculatedStock);
        materialDocumentRepository.save(materialDocument);
    }

    @Override
    public void save(MaterialReserved materialReserved) {
        MaterialDocument materialDocument = materialDocumentRepository.findByReference(materialReserved.getMaterialReference().getValue())
                .orElseThrow(() -> new MaterialNotFoundException(String.format("Material with reference %s not found", materialReserved.getMaterialReference().getValue())));
        UUID ticketReference = materialReserved.getTicketReference().getValue();
        TicketMaterialDocumentPart ticketMaterial = getTicketMaterial(materialDocument, ticketReference);
        Integer calculatedReserved = ticketMaterial.getReserved() + materialReserved.getReservedQuantity().getValue();
        ticketMaterial.setReserved(calculatedReserved);
        materialDocumentRepository.save(materialDocument);
    }

    @Override
    public void save(MaterialUsed materialUsed) {
        MaterialDocument materialDocument = materialDocumentRepository.findByReference(materialUsed.getMaterialReference().getValue())
                .orElseThrow(() -> new MaterialNotFoundException(String.format("Material with reference %s not found", materialUsed.getMaterialReference().getValue())));
        UUID ticketReference = materialUsed.getTicketReference().getValue();
        TicketMaterialDocumentPart ticketMaterial = getTicketMaterial(materialDocument, ticketReference);
        Integer calculatedUsed = ticketMaterial.getUsed() + materialUsed.getUsedQuantity().getValue();
        ticketMaterial.setUsed(calculatedUsed);
        materialDocumentRepository.save(materialDocument);
    }

    @Override
    public void save(MaterialSentBack materialSentBack) {
        MaterialDocument materialDocument = materialDocumentRepository.findByReference(materialSentBack.getMaterialReference().getValue())
                .orElseThrow(() -> new MaterialNotFoundException(String.format("Material with reference %s not found", materialSentBack.getMaterialReference().getValue())));
        UUID ticketReference = materialSentBack.getTicketReference().getValue();
        TicketMaterialDocumentPart ticketMaterial = getTicketMaterial(materialDocument, ticketReference);
        Integer calculatedUsed = ticketMaterial.getUsed() - materialSentBack.getSentBackQuantity().getValue();
        ticketMaterial.setUsed(calculatedUsed);
        materialDocumentRepository.save(materialDocument);
    }

    private TicketMaterialDocumentPart getTicketMaterial(MaterialDocument materialDocument, UUID ticketReference) {
        Map<UUID,TicketMaterialDocumentPart> materialByTicket = materialDocument.getMaterialByTicket();
        return materialByTicket.computeIfAbsent(ticketReference, key -> new TicketMaterialDocumentPart());
    }

    @Override
    public List<MaterialView> getAllMaterial() {
        return materialDocumentRepository.findAll().stream()
                .map(it -> MaterialView.of(
                        MaterialReference.of(it.getReference()),
                        MaterialDescription.of(it.getDescription()),
                        MaterialQuantity.of(it.getInStock()),
                        it.getMaterialByTicket().entrySet().stream()
                                .map(entryset -> convert(entryset))
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    private MaterialView convert(MaterialDocument materialDocument) {
        return MaterialView.of(
        MaterialReference.of(materialDocument.getReference()),
                MaterialDescription.of(materialDocument.getDescription()),
                MaterialQuantity.of(materialDocument.getInStock()),
                materialDocument.getMaterialByTicket().entrySet().stream()
                        .map(entryset -> convert(entryset))
                        .collect(Collectors.toSet())
        );
    }
    @Override
    public MaterialView getMaterialByReference(MaterialReference materialReference) {
        Optional<MaterialDocument> materialDocumentOptional = materialDocumentRepository.findByReference(materialReference.getValue());
        if (materialDocumentOptional.isPresent()) {
            return convert(materialDocumentOptional.get());
        }
        throw new MaterialNotFoundException(String.format("Material with reference: %s not found!", materialReference.getValue()));
    }

    private Map.Entry<TicketReference,TicketMaterialView> convert(Map.Entry<UUID,TicketMaterialDocumentPart> materialByTicket) {
        UUID key = materialByTicket.getKey();
        TicketMaterialDocumentPart value = materialByTicket.getValue();
        return Map.entry(
                TicketReference.of(key),
                TicketMaterialView.of(
                        MaterialQuantity.of(value.getReserved()),
                        MaterialQuantity.of(value.getUsed()))
        );
    }
}
