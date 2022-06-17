package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MaterialViewService {

    private final MaterialViewStorePort materialViewStorePort;

    public List<MaterialView> getAllMaterial() {
        return materialViewStorePort.getAllMaterial();
    }

    public MaterialView getMaterialByReference(MaterialReference materialReference) {
        return materialViewStorePort.getMaterialByReference(materialReference);
    }
}
