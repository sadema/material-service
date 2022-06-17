package nl.kristalsoftware.ddd.materialservice.rest.material.command;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UseMaterialForTicketRequestBody {
    @NotNull
    private Integer materialQuantity;
    @NotNull
    private UUID ticketReference;
}
