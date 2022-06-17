package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects;

import nl.kristalsoftware.ddd.domain.base.TinyStringType;

public class MaterialDescription extends TinyStringType {

    private MaterialDescription(String value) {
        super(value);
    }

    public static MaterialDescription of(String value) {
        return new MaterialDescription(value);
    }

}
