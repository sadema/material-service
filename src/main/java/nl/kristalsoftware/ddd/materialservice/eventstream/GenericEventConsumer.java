package nl.kristalsoftware.ddd.materialservice.eventstream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.domain.application.aggregate.material.eventstore.AggregateNotFoundException;
import nl.kristalsoftware.ddd.materialservice.eventstream.offsetmanagement.TopicPartitionHandler;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenericEventConsumer<T,V extends SpecificRecord> {

    private TopicPartitionHandler topicPartitionHandler;

    @Autowired
    public final void setTopicPartitionHandler(TopicPartitionHandler topicPartitionHandler) {
        this.topicPartitionHandler = topicPartitionHandler;
    }

    @Transactional
    protected void consumeData(KafkaDomainEventHandler<T,V> kafkaDomainEventHandler, ConsumerRecord<T,V> record) {
        log.info("Key: {}, Value: {}, Partition: {}, Offset: {}",
                record.key(), record.value(), record.partition(), record.offset());
        try {
            kafkaDomainEventHandler.save(record.value());
            topicPartitionHandler.save(record.topic(), record.partition(), record.offset());
        } catch(AggregateNotFoundException e) {
            e.printStackTrace();
        }
    }

}
