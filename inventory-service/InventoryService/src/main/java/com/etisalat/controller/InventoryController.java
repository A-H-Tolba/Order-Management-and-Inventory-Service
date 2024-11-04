package com.etisalat.controller;

import com.etisalat.dto.InventoryDto;
import com.etisalat.service.InventoryService;
import java.util.Map;
import java.util.UUID;
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
@RequestMapping("/inventories")
@AllArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @GetMapping("/retrieve")
    public ResponseEntity<Page<InventoryDto>> retrieveInventories(Pageable pageable)
    {
        return new ResponseEntity<>(inventoryService.retrieveInventories(pageable), HttpStatus.ACCEPTED);
    }
    
    @PostMapping("validateAndReserve")
    public ResponseEntity<Boolean> validateAndReserve(Map<UUID, Integer> orderItems_quantities)
    {
        try {
            return new ResponseEntity<>(inventoryService.validateAndReserve(orderItems_quantities), HttpStatus.ACCEPTED);
        } catch(Exception e)
        {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.ACCEPTED);
        }
    }
}
