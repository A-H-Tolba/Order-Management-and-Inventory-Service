package com.etisalat.service;

import com.etisalat.config.KafkaProducer;
import com.etisalat.dto.OrderItemsDto;
import com.etisalat.model.OderItemsModel;
import com.etisalat.model.OrderModel;
import com.etisalat.ref.OrderStatusRef;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import repository.OrderRepository;

/**
 *
 * @author Ahmed Tolba
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;
    
    @Autowired
    RestTemplate restTemplate;

    @Override
    public void placeOrder(OrderModel orderModel) {
        Map<UUID, Integer> orderItems_quantities = orderModel.getOrItemsModels().stream().collect(Collectors.toMap(OderItemsModel::getItemCode, OderItemsModel::getQuantity));
        if(inventoryValidateAndReserve(orderItems_quantities))
        {
            orderModel.setStatus(OrderStatusRef.PENDING);
            orderRepository.save(orderModel);
            kafkaProducer.sendMessage("orderCreatedTopic", new OrderItemsDto(orderModel.getId(), orderItems_quantities));
        }
        else
        {
            orderModel.setStatus(OrderStatusRef.FAILED);
            orderRepository.save(orderModel);
        }
    }
    
    @Value("${inventory-ms.endpoint}")
    private String inventoryMsEndpoint;
    
    private Boolean inventoryValidateAndReserve(Map<UUID, Integer> orderItems_quantities)
    {
        try
        {
            URI uri = new URI(inventoryMsEndpoint + "/validateAndReserve/");
            ResponseEntity<Boolean> response = restTemplate.postForEntity(uri, orderItems_quantities, Boolean.class);
            return response.getBody();
        } catch (URISyntaxException ex)
        {
            log.error(ex.getMessage());
            return false;
        }
    }
    
    @Override
    public void consumeInventoryCreated(Map<Long, Boolean> inventoryCreated) {
        Map.Entry<Long, Boolean> entry = inventoryCreated.entrySet().iterator().next();
        Long id = entry.getKey();
        Boolean createdSuccessfully = entry.getValue();
        Optional<OrderModel> optionalOrderModel = orderRepository.findById(id);
        if(optionalOrderModel.isPresent())
        {
            OrderModel orderModel = optionalOrderModel.get();
            orderModel.setStatus((createdSuccessfully) ? OrderStatusRef.SUCCESSFUL : OrderStatusRef.FAILED);
            orderRepository.save(orderModel);
        }
    }
    
    @Override
    public OrderModel retrieveOrder(Long id) {
        Optional<OrderModel> orderModel = orderRepository.findById(id);
        if(orderModel.isPresent())
            return orderModel.get();
        else
            throw new UnsupportedOperationException("Order Not Found");
    }

    @Override
    public Page<OrderModel> retrieveOrders(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new UnsupportedOperationException("Not Authenticated");
        String username = authentication.getName();
        return orderRepository.findByUser_UserName(username, pageable);
    }

    @Override
    public void failTimedOutOrders() {
        List<OrderModel> orderModels = orderRepository.findByCreatedAtBefore(LocalDateTime.now().minusMinutes(30));
        orderModels.forEach(order -> order.setStatus(OrderStatusRef.FAILED));
        orderRepository.saveAll(orderModels);
    }
    
}
