package com.etisalat.service;

import com.etisalat.model.OrderModel;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

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
    
    @Scheduled(cron = "*/1 * * * * *")
    @Async
    void failTimedOutOrders();
}
