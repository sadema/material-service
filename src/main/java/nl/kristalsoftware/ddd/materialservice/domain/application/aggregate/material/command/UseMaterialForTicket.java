package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class UseMaterialForTicket {

    private final MaterialQuantity usedQuantity;
    private final TicketReference ticketReference;

}
