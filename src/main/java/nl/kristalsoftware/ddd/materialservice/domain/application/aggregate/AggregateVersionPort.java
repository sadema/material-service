package nl.kristalsoftware.ddd.materialservice.domain.application.aggregate;

public interface AggregateVersionPort<T> {
    Long getVersion(T reference);
}
