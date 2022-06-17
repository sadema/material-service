package nl.kristalsoftware.ddd.materialservice.viewstore.material;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MaterialDocumentRepository extends MongoRepository<MaterialDocument, ObjectId> {
    Optional<MaterialDocument> findByReference(UUID value);

}
