package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects;

import nl.kristalsoftware.ddd.domain.base.TinyIntegerType;

public class MaterialQuantity extends TinyIntegerType<MaterialQuantity> {

    private MaterialQuantity(Integer value) {
        super(value);
    }

    public static MaterialQuantity of(Integer value) {
        return new MaterialQuantity(value);
    }

    @Override
    protected MaterialQuantity create(int value) {
        return MaterialQuantity.of(value);
    }

}
