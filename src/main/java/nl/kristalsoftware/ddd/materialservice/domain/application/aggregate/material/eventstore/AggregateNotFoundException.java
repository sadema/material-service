package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore;

public class AggregateNotFoundException extends Throwable {
    public AggregateNotFoundException(String message) {
        super(message);
    }
}
