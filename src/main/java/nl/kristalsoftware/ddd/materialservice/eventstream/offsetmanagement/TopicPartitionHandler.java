package nl.kristalsoftware.ddd.materialservice.eventstream.offsetmanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TopicPartitionHandler {

    private final JpaTopicPartitionRepository topicPartitionRepository;

    public void save(String topic, int partition, long offset) {
        Optional<TopicPartitionData> topicPartitionDataOptional =
                topicPartitionRepository.findOneByTopicNameAndPartition(topic, partition);
        if (topicPartitionDataOptional.isPresent()) {
            TopicPartitionData topicPartitionData = topicPartitionDataOptional.get();
            topicPartitionData.setOffset(offset + 1);
        }
        else {
            TopicPartitionData topicPartitionData = TopicPartitionData.of(
                    topic,
                    partition,
                    1L
            );
            topicPartitionRepository.save(topicPartitionData);
        }
    }
}
