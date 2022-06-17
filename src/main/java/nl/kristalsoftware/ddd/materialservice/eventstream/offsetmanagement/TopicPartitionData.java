package nl.kristalsoftware.ddd.materialservice.eventstream.offsetmanagement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "topicpartition")
public class TopicPartitionData {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String topicName;

    private Integer partition;

    @Setter
    @Column(name = "partition_offset")
    private Long offset;

    public static TopicPartitionData of(String topicName, Integer partition, Long offset) {
        return new TopicPartitionData(null, topicName, partition, offset);
    }

}
