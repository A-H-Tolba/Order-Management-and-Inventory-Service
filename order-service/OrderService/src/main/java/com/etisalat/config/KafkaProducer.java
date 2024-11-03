package com.etisalat.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
/**
 *
 * @author Ahmed Tolba
 */
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, String message) {
        kafkaTemplate.send(topicName, message);
    }
}
