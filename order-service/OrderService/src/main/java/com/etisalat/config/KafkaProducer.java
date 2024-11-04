package com.etisalat.config;

import com.etisalat.dto.OrderItemsDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
/**
 *
 * @author Ahmed Tolba
 */
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, OrderItemsDto> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, OrderItemsDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topicName, OrderItemsDto message) {
        kafkaTemplate.send(topicName, message);
    }
}
