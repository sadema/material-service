package nl.kristalsoftware.ddd.materialservice.eventstream.offsetmanagement;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OffsetManager implements ConsumerAwareRebalanceListener {

    private final JpaTopicPartitionRepository topicPartitionRepository;

    @Override
    public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> topicPartitions) {
        for (TopicPartition topicPartition : topicPartitions) {
            Optional<TopicPartitionData> topicPartitionDataOptional =
                    topicPartitionRepository.findOneByTopicNameAndPartition(topicPartition.topic(), topicPartition.partition());
            topicPartitionDataOptional.ifPresent(it -> {
                consumer.seek(topicPartition, topicPartitionDataOptional.get().getOffset());
            });
        }
    }

}
