package nl.kristalsoftware.ddd.materialservice.rest.material.command.requestbody;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SendMaterialRetourForTicketRequestBody {
    @NotNull
    private Integer materialQuantity;
    @NotNull
    private UUID ticketReference;
}
