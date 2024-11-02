package com.etisalat.controller;

import com.etisalat.dto.OrderDto;
import com.etisalat.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import java.awt.print.Pageable;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Void> placeOrder(OrderDto orderDto)
    {
        orderService.placeOrder(orderDto);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "Retrieve user's orders")
    @GetMapping("/retrieve")
    public ResponseEntity<Page<OrderDto>> retrieveOrders(Pageable pageable)
    {
        return new ResponseEntity<>(orderService.retrieveOrders(pageable), HttpStatus.ACCEPTED);
    }
}
