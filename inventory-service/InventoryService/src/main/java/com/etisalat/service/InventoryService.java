package com.etisalat.service;

import com.etisalat.dto.InventoryDto;
import java.awt.print.Pageable;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ahmed Tolba
 */
@Service
public interface InventoryService {
    Page<InventoryDto> retrieveInventories(Pageable pageable);
    Boolean validateAndReserve(Map<UUID, Integer> orderItems_quantities);
}
