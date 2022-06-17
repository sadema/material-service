package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class AddMaterialToStock {
    private final MaterialQuantity materialQuantity;
}
