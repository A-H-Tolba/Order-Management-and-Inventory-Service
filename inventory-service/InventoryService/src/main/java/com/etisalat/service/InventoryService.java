package com.etisalat.service;

import com.etisalat.dto.InventoryDto;
import com.etisalat.dto.OrderItemsDto;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ahmed Tolba
 */
@Service
public interface InventoryService {
    Page<InventoryDto> retrieveInventories(Pageable pageable);
    Boolean validateAndReserve(Map<UUID, Integer> orderItems_quantities);
    @KafkaListener(topics = "orderCreatedTopic", groupId = "${spring.application.name}")
    void consumeInventoryCreated(OrderItemsDto orderItemsDto);
    
    @Scheduled(cron = "0 0/30 * * * *")
    @Async
    void failTimedOutReserves();
}
