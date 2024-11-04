package com.etisalat.service;

import com.etisalat.model.OrderModel;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;

/**
 *
 * @author Ahmed Tolba
 */
public interface OrderService {
    void placeOrder(OrderModel orderModel);
    @KafkaListener(topics = "inventoryCreatedTopic", groupId = "${spring.application.name}")
    void consumeInventoryCreated(Map<Long, Boolean> inventoryCreated);
    OrderModel retrieveOrder(Long id);
    Page<OrderModel> retrieveOrders(Pageable pageable);
}
