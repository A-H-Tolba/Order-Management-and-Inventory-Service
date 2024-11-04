package com.etisalat.controller;

import com.etisalat.model.OrderModel;
import com.etisalat.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ahmed Tolba
 */
@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @Operation(summary = "Place an order")
    @PostMapping("/place")
    public ResponseEntity<Void> placeOrder(OrderModel orderDto)
    {
        orderService.placeOrder(orderDto);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "Retrieve certain order", description = "Used to retrieve a specific order and to also learn the order status right after it was created.")
    @GetMapping("/retrieve/{id}")
    public ResponseEntity<OrderModel> retrieveOrder(Long id) {
        return new ResponseEntity<>(orderService.retrieveOrder(id), HttpStatus.ACCEPTED);
    }
    
    //So far the OrderDto and the OrderModel would be identical. They can be separated and mapped however if desired
    @Operation(summary = "Retrieve user's orders")
    @GetMapping("/retrieve")
    public ResponseEntity<Page<OrderModel>> retrieveOrders(Pageable pageable)
    {
        return new ResponseEntity<>(orderService.retrieveOrders(pageable), HttpStatus.ACCEPTED);
    }
}
