package nl.kristalsoftware.ddd.materialservice.rest.material.command;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterMaterialRequestBody {
    @NotBlank
    private String description;

    @Min(1)
    private Integer inStock;
}
