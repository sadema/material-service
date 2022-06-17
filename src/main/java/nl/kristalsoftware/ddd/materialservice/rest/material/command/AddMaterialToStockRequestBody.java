package nl.kristalsoftware.ddd.materialservice.rest.material.command;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddMaterialToStockRequestBody {
    @NotNull
    private Integer materialQuantity;
}
