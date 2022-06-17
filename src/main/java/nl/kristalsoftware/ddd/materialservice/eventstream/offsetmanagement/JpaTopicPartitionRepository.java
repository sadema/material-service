package nl.kristalsoftware.ddd.materialservice.eventstream.offsetmanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTopicPartitionRepository extends JpaRepository<TopicPartitionData,Long> {

    Optional<TopicPartitionData> findOneByTopicNameAndPartition(String topicName, Integer partition);

}
