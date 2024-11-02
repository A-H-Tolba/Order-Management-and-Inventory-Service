package com.etisalat.service;

import com.etisalat.dto.OrderDto;
import java.awt.print.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ahmed Tolba
 */
@Service
public interface OrderService {
    void placeOrder(OrderDto orderDto);
    Page<OrderDto> retrieveOrders(Pageable pageable);
}
