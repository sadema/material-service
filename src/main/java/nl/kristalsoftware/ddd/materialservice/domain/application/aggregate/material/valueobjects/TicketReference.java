package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects;

import lombok.Getter;
import nl.kristalsoftware.ddd.domain.base.TinyUUIDType;

import java.util.UUID;

@Getter
public class TicketReference extends TinyUUIDType {

    private TicketReference(UUID value) {
        super(value);
    }

    public static TicketReference of(UUID value) {
        return new TicketReference(value);
    }
}
