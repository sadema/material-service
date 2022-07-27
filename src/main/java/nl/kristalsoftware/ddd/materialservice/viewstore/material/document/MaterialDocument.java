package nl.kristalsoftware.ddd.materialservice.viewstore.material.document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document
public class MaterialDocument {

    @Setter(AccessLevel.NONE)
    @Id
    private ObjectId _id;
    private UUID reference;
    private String description;
    private Integer inStock;
    @Version
    private Long version;

    private Map<UUID, MaterialDocumentTicketPart> materialByTicket;

    private MaterialDocument(UUID reference, String description, Integer inStock) {
        this.reference = reference;
        this.description = description;
        this.inStock = inStock;
        this.materialByTicket = new HashMap<>();
    }

    public static MaterialDocument of(UUID reference, String description, Integer inStock) {
        return new MaterialDocument(reference, description, inStock);
    }

}
