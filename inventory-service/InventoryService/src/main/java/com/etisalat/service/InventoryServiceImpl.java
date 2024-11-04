package com.etisalat.service;

import com.etisalat.dto.InventoryDto;
import com.etisalat.exceptions.ItemNotFoundException;
import com.etisalat.model.ItemModel;
import com.etisalat.repository.ItemRepository;
import java.awt.print.Pageable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ahmed Tolba
 */
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ItemRepository itemRepository;
    @Override
    public Page<InventoryDto> retrieveInventories(Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    @Transactional(rollbackFor = ItemNotFoundException.class)
    public Boolean validateAndReserve(Map<UUID, Integer> orderItems_quantities) {
        for(Map.Entry<UUID, Integer> entry : orderItems_quantities.entrySet())
        {
            UUID orderId = entry.getKey();
            Integer quantity = entry.getValue();
            Optional<ItemModel> optionalItemModel = itemRepository.findById(orderId);
            if(optionalItemModel.isPresent())
            {
                ItemModel itemModel = optionalItemModel.get();
                itemModel.setInStock(itemModel.getInStock() - quantity);
                itemModel.setReserved(itemModel.getReserved() + quantity);
                itemRepository.save(itemModel);
            }
            else throw new ItemNotFoundException("Item With ID '" + orderId + "' Doesn't Exist");
        }
        return true;
    }
    
}
