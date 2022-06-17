package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects;

import lombok.Getter;
import nl.kristalsoftware.ddd.domain.base.TinyUUIDType;

import java.util.UUID;

@Getter
public class MaterialReference extends TinyUUIDType {

    private MaterialReference(UUID value) {
        super(value);
    }

    public static MaterialReference of(UUID value) {
        return new MaterialReference(value);
    }
}
