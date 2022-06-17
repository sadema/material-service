package nl.kristalsoftware.ddd.materialservice.eventstream.material.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kristalsoftware.ddd.materialservice.eventstream.GenericEventConsumer;
import nl.kristalsoftware.ddd.materialservice.eventstream.KafkaDomainEventHandler;
import nl.kristalsoftware.ddd.materialservice.eventstream.KafkaDomainEventHandlerProvider;
import nl.kristalsoftware.ddd.retourservice.material.MaterialRetourEventData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class RetourDomainEventConsumer extends GenericEventConsumer<String,MaterialRetourEventData> {

    private final KafkaDomainEventHandlerProvider<String, MaterialRetourEventData> kafkaDomainEventHandlerProvider;

    @Transactional
//    @KafkaListener(topics = "${material.kafka.retour.topicname}")
    public void consumeData(@Payload ConsumerRecord<String, MaterialRetourEventData> record) {
        log.info("Player: Key: {}, Value: {}, Partition: {}, Offset: {}",
                record.partition(), record.offset(), record.key(), record.value());
        KafkaDomainEventHandler<String,MaterialRetourEventData> kafkaDomainEventHandler = kafkaDomainEventHandlerProvider.getEventMessageHandler(record.value().getDomainEventName());
        if (kafkaDomainEventHandler != null) {
            super.consumeData(kafkaDomainEventHandler, record);
        }
        else {
            throw new IllegalStateException("No eventMessageHandler found!");
        }
    }

}

