package nl.kristalsoftware.ddd.materialservice.eventstream.material.consumer;

import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.command.MaterialCommandService;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialQuantity;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.MaterialReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.eventstream.KafkaDomainEventHandler;
import nl.kristalsoftware.ddd.retourservice.material.MaterialRetourEventData;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MaterialReceivedRetourHandler implements KafkaDomainEventHandler<String, MaterialRetourEventData> {

    private MaterialCommandService materialCommandService;

    @Override
    public String appliesTo() {
        return "MaterialReceivedRetour";
    }

    @Override
    public void save(MaterialRetourEventData message) throws AggregateNotFoundException {
        materialCommandService.sendMaterialRetourForTicket(
                MaterialReference.of(message.getReference()),
                MaterialQuantity.of(((Long) message.getQuantity()).intValue()),
                TicketReference.of(message.getTicketreference())
        );
    }
}
