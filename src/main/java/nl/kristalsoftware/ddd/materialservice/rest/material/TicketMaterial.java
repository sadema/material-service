package nl.kristalsoftware.ddd.materialservice.rest.material;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor(staticName = "of")
@Data
public class TicketMaterial {
    private UUID ticketReference;
    private Integer reserved;
    private Integer used;

}
