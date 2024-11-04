package com.etisalat.config;

import java.util.Map;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ahmed Tolba
 */
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, Map<Long, Boolean>> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Map<Long, Boolean>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, Map<Long, Boolean> inventoryCreated) {
        kafkaTemplate.send(topicName, inventoryCreated);
    }
}
