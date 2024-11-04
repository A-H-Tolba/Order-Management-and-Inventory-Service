package com.etisalat.service;

import com.etisalat.config.KafkaProducer;
import com.etisalat.dto.OrderItemsDto;
import com.etisalat.model.OderItemsModel;
import com.etisalat.model.OrderModel;
import com.etisalat.ref.OrderStatusRef;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import repository.OrderRepository;

/**
 *
 * @author Ahmed Tolba
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Pageable pageable;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderModel orderModel;
    private OderItemsModel orderItem;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "1234"));
        orderModel = new OrderModel();
        orderModel.setId(1L);
        orderModel.setOrderNumber("ORDER123");
        orderModel.setStatus(OrderStatusRef.PENDING);
        
        orderItem = new OderItemsModel();
        orderItem.setItemCode(UUID.randomUUID());
        orderItem.setQuantity(5);
        
        orderModel.setOrItemsModels(Set.of(orderItem));
        
        ReflectionTestUtils.setField(orderService, "inventoryMsEndpoint", "http://localhost:8086");
    }
    
    @Test
    void testPlaceOrder_SuccessfulInventoryValidation() throws Exception {
        Mockito.when(restTemplate.postForEntity(Mockito.any(URI.class), Mockito.any(), ArgumentMatchers.any(Class.class)))
            .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));
        
        // Setting up expected orderItemsQuantities
        Map<UUID, Integer> expectedOrderItemsQuantities = orderModel.getOrItemsModels()
            .stream()
            .collect(Collectors.toMap(OderItemsModel::getItemCode, OderItemsModel::getQuantity));

        // Place the order
        orderService.placeOrder(orderModel);

        // Assert order status is set to PENDING
        Assertions.assertEquals(OrderStatusRef.PENDING, orderModel.getStatus());
        Mockito.verify(orderRepository).save(orderModel);

        // Capture the DTO sent to Kafka
        ArgumentCaptor<OrderItemsDto> kafkaMessageCaptor = ArgumentCaptor.forClass(OrderItemsDto.class);
        Mockito.verify(kafkaProducer).sendMessage(Mockito.eq("orderCreatedTopic"), kafkaMessageCaptor.capture());

        // Assert that the captured Kafka message DTO contains the correct orderId and orderItemsQuantities
        OrderItemsDto capturedMessage = kafkaMessageCaptor.getValue();
        Assertions.assertEquals(orderModel.getId(), capturedMessage.getOrderId());
        Assertions.assertEquals(expectedOrderItemsQuantities, capturedMessage.getItems());
    }

    @Test
    void testPlaceOrder_FailedInventoryValidation() {
        Mockito.when(restTemplate.postForEntity(Mockito.any(URI.class), Mockito.any(), Mockito.eq(Boolean.class)))
            .thenReturn(new ResponseEntity<>(false, HttpStatus.OK));

        orderService.placeOrder(orderModel);
        
        Assertions.assertEquals(OrderStatusRef.FAILED, orderModel.getStatus());
        Mockito.verify(orderRepository).save(orderModel);
        Mockito.verify(kafkaProducer, Mockito.never()).sendMessage(Mockito.anyString(), Mockito.any());
    }

    @Test
    void testConsumeInventoryCreated_OrderExists_SuccessfulInventory() {
        Mockito.when(orderRepository.findById(orderModel.getId())).thenReturn(Optional.of(orderModel));
        
        Map<Long, Boolean> inventoryCreated = new HashMap<>();
        inventoryCreated.put(orderModel.getId(), true);
        
        orderService.consumeInventoryCreated(inventoryCreated);
        
        Assertions.assertEquals(OrderStatusRef.SUCCESSFUL, orderModel.getStatus());
        Mockito.verify(orderRepository).save(orderModel);
    }

    @Test
    void testConsumeInventoryCreated_OrderExists_FailedInventory() {
        Mockito.when(orderRepository.findById(orderModel.getId())).thenReturn(Optional.of(orderModel));

        Map<Long, Boolean> inventoryCreated = new HashMap<>();
        inventoryCreated.put(orderModel.getId(), false);

        orderService.consumeInventoryCreated(inventoryCreated);

        Assertions.assertEquals(OrderStatusRef.FAILED, orderModel.getStatus());
        Mockito.verify(orderRepository).save(orderModel);
    }

    @Test
    void testConsumeInventoryCreated_OrderNotFound() {
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Map<Long, Boolean> inventoryCreated = new HashMap<>();
        inventoryCreated.put(99L, true);  // Order ID does not exist

        orderService.consumeInventoryCreated(inventoryCreated);

        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testRetrieveOrder_OrderExists() {
        Mockito.when(orderRepository.findById(orderModel.getId())).thenReturn(Optional.of(orderModel));

        OrderModel result = orderService.retrieveOrder(orderModel.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderModel.getId(), result.getId());
    }

    @Test
    void testRetrieveOrder_OrderNotFound() {
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(UnsupportedOperationException.class, () -> orderService.retrieveOrder(99L));
    }
    
    @Test
    public void retrieveOrders_authenticatedUser_returnsOrders() {
        Page<OrderModel> orderPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(orderRepository.findByUser_UserName("testuser", pageable)).thenReturn(orderPage);

        // Act
        Page<OrderModel> result = orderService.retrieveOrders(pageable);

        // Assert
        Mockito.verify(orderRepository, Mockito.times(1)).findByUser_UserName("testuser", pageable);
        Assertions.assertNotNull(result);
    }

}
