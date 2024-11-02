package com.etisalat.service;

import com.etisalat.model.OrderModel;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @InjectMocks
    private OrderServiceImpl orderService;
    
    @BeforeEach
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("testuser", "1234"));
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
