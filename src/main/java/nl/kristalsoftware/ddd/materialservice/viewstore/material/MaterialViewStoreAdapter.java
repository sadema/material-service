package nl.kristalsoftware.ddd.materialservice.viewstore.material;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.AggregateVersionPort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.Material;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.event.MaterialDomainEvent;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialView;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialViewStorePort;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.TicketMaterialView;
import nl.kristalsoftware.ddd.materialservice.viewstore.material.document.MaterialDocument;
import nl.kristalsoftware.ddd.materialservice.viewstore.material.document.MaterialDocumentRepository;
import nl.kristalsoftware.ddd.materialservice.viewstore.material.document.MaterialDocumentTicketPart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MaterialViewStoreAdapter implements MaterialViewStorePort, AggregateVersionPort<MaterialReference> {

    private final MaterialDocumentRepository materialDocumentRepository;

    @Override
    public void save(List<MaterialDomainEvent> domainEventList, Material material) {
        MaterialDocumentPersistenceAdapter materialDocumentPersistenceAdapter = new MaterialDocumentPersistenceAdapter(materialDocumentRepository, material);
        materialDocumentPersistenceAdapter.updateDocument(domainEventList);
    }

    @Deprecated
    @Override
    public Long getVersion(MaterialReference reference) {
        MaterialDocument materialDocument = materialDocumentRepository.findByReference(reference.getValue())
                .orElseThrow(() -> new MaterialNotFoundException(String.format("Material with reference %s not found", reference.getValue())));
        return materialDocument.getVersion();
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

    public MaterialView getMaterialByReference(MaterialReference materialReference) {
        Optional<MaterialDocument> materialDocumentOptional = materialDocumentRepository.findByReference(materialReference.getValue());
        if (materialDocumentOptional.isPresent()) {
            return convert(materialDocumentOptional.get());
        }
        throw new MaterialNotFoundException(String.format("Material with reference: %s not found!", materialReference.getValue()));
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

    private Map.Entry<TicketReference, TicketMaterialView> convert(Map.Entry<UUID, MaterialDocumentTicketPart> materialByTicket) {
        UUID key = materialByTicket.getKey();
        MaterialDocumentTicketPart value = materialByTicket.getValue();
        return Map.entry(
                TicketReference.of(key),
                TicketMaterialView.of(
                        MaterialQuantity.of(value.getReserved()),
                        MaterialQuantity.of(value.getUsed()))
        );
    }

}
