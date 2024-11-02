package com.etisalat.service;

import com.etisalat.dto.InventoryDto;
import java.awt.print.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ahmed Tolba
 */
@Service
public interface InventoryService {
    Page<InventoryDto> retrieveInventorys(Pageable pageable);
}
