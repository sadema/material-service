package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialDescription;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.rest.material.command.RegisterMaterialConstraint;

@Getter
@RequiredArgsConstructor(staticName = "of")
@RegisterMaterialConstraint
public class RegisterMaterial {

    private final MaterialDescription materialDescription;
    private final MaterialQuantity materialQuantity;

}
