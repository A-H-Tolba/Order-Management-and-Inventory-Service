package com.etisalat.service;

import com.etisalat.model.OrderModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.OrderRepository;

/**
 *
 * @author Ahmed Tolba
 */
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;

    @Override
    public void placeOrder(OrderModel orderDto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Page<OrderModel> retrieveOrders(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null)
            throw new UnsupportedOperationException("Not Authenticated");
        String username = authentication.getName();
        return orderRepository.findByUser_UserName(username, pageable);
    }
    
}
