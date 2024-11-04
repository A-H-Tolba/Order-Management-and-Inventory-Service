package com.etisalat.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
/**
 *
 * @author Ahmed Tolba
 */
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, Long> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, Long message) {
        kafkaTemplate.send(topicName, message);
    }
}
