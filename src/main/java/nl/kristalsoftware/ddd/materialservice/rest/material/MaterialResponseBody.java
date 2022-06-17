package nl.kristalsoftware.ddd.materialservice.rest.material;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.valueobjects.TicketReference;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.MaterialView;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.viewport.TicketMaterialView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MaterialResponseBody {

    private final UUID materialReference;
    private final String materialDescription;
    private final Integer materialItemsInStock;
    private final List<TicketMaterial> materialByTicket;

    public static MaterialResponseBody of(MaterialView it) {
        return new MaterialResponseBody(
                it.getMaterialReference().getValue(),
                it.getMaterialDescription().getValue(),
                it.getMaterialItemsInStock().getValue(),
                convert(it.getMaterialByTicket())
        );
    }

    private static List<TicketMaterial> convert(Set<Map.Entry<TicketReference, TicketMaterialView>> materialByTicket) {
        return materialByTicket.stream()
                .map(it -> convert(it.getKey(), it.getValue()))
                .collect(Collectors.toList());
    }

    private static TicketMaterial convert(TicketReference key, TicketMaterialView value) {
        return TicketMaterial.of(key.getValue(), value.getReserved().getValue(), value.getUsed().getValue());
    }
}
