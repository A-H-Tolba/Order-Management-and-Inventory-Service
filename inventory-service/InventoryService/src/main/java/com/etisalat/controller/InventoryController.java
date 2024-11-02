package com.etisalat.controller;

import com.etisalat.dto.InventoryDto;
import com.etisalat.service.InventoryService;
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
@RequestMapping("/inventories")
@AllArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @GetMapping("/retrieve")
    public ResponseEntity<Page<InventoryDto>> retrieveInventorys(Pageable pageable)
    {
        return new ResponseEntity<>(inventoryService.retrieveInventorys(pageable), HttpStatus.ACCEPTED);
    }
}
