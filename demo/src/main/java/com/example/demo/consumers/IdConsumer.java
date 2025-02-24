package com.example.demo.consumers;
import com.example.demo.services.IdService;
import com.example.demo.utils.PartitionMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class IdConsumer {

//    private static final String TOPIC = "id-retrieval-requests";
    private static final String TOPIC = "id-test-partitions";

    private static final Logger log = LoggerFactory.getLogger(IdConsumer.class);
    private static final int MAX_RETRY_ATTEMPTS = 5;  // Maximum number of retries
    private static final long RETRY_DELAY_MS = 1000;  // 1 second delay between retries


    private final KafkaConsumer<String, String> consumer;

    @Autowired
    public IdConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
//        this.consumer.subscribe(Collections.singletonList(TOPIC));
    }


public String pollMessages(String airlineDesignator) {
    int attempts = 0;

    // Get partition based on the airline designator
    int partitionNumber = PartitionMapper.getPartitionForAirline(airlineDesignator, 5);

    TopicPartition partition = new TopicPartition(TOPIC, partitionNumber);
    consumer.assign(Collections.singletonList(partition));

    // Seek to the last committed offset or start from beginning
    consumer.seek(partition, consumer.committed(partition) != null
            ? consumer.committed(partition).offset()
            : 0L);

    while (attempts < MAX_RETRY_ATTEMPTS) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));

        if (!records.isEmpty()) {
            log.info("Received {} records", records.count());

            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Received record:");
                System.out.println("Key: " + record.key());
                System.out.println("Value: " + record.value());
                System.out.println("Partition: " + record.partition());
                System.out.println("Offset: " + record.offset());
                System.out.println("---------------------------------");

                // Commit offset manually after successful processing
                consumer.commitSync(Collections.singletonMap(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1)
                ));

                return record.value(); // Return the first PNR immediately
            }
        }

        log.warn("No PNR retrieved, retrying attempt {}/{}", attempts + 1, MAX_RETRY_ATTEMPTS);
        attempts++;

        try {
            Thread.sleep(RETRY_DELAY_MS); // Wait before retrying
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Polling retry interrupted", e);
            break;
        }
    }

    log.error("Failed to retrieve a record from partition {} after {} attempts", partitionNumber, MAX_RETRY_ATTEMPTS);
    return null;  // Return null if no PNR was obtained after retries
    }
}

