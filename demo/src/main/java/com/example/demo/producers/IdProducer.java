package com.example.demo.producers;

import com.example.demo.utils.PartitionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IdProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "id-test-partitions";
//    private static final String TOPIC = "id-retrieval-requests";

    public IdProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendIdRequest(String airlineDesignator, String requestId) {
        // Get partition based on the airline designator
        int partitionNumber = PartitionMapper.getPartitionForAirline(airlineDesignator, 5);

        String key = "k-" + requestId;

        kafkaTemplate.send(TOPIC, partitionNumber, key, requestId);
        log.info("Sent request: " + requestId);
    }
}
