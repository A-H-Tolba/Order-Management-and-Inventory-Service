package com.etisalat.service;

import com.etisalat.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Ahmed Tolba
 */
public interface OrderService {
    void placeOrder(OrderModel orderDto);
    Page<OrderModel> retrieveOrders(Pageable pageable);
}
