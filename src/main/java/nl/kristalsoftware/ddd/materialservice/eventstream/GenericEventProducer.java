package nl.kristalsoftware.ddd.materialservice.eventstream;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class GenericEventProducer<T extends SpecificRecord> {

    public void produceEvent(
            KafkaTemplate<String, T> kafkaTemplate,
            String topicname,
            String key,
            T eventData) {

        ListenableFuture<SendResult<String, T>> future = kafkaTemplate.send(
                topicname,
                key,
                eventData);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("An error occurred");
            }

            @Override
            public void onSuccess(SendResult<String, T> sendResult) {
                RecordMetadata metaData = sendResult.getRecordMetadata();
                log.info("Received new metadata. \n" +
                        "Topic: " + metaData.topic() + "\n" +
                        "Partition: " + metaData.partition() + "\n" +
                        "Offset: " + metaData.offset() + "\n" +
                        "Timestamp: " + metaData.timestamp()
                );
                log.info(sendResult.getProducerRecord().value().toString());
            }
        });
    }

}
