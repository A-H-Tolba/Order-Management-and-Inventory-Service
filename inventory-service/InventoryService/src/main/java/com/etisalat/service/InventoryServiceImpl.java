package com.etisalat.service;

import com.etisalat.config.KafkaProducer;
import com.etisalat.dto.InventoryDto;
import com.etisalat.dto.OrderItemsDto;
import com.etisalat.exceptions.ItemNotFoundException;
import com.etisalat.model.ItemModel;
import com.etisalat.repository.ItemRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ahmed Tolba
 */
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ItemRepository itemRepository;
    private final KafkaProducer kafkaProducer;
    
    @Override
    public Page<InventoryDto> retrieveInventories(Pageable pageable) {
        return itemRepository.findAll(pageable).map(this::toDto);
    }
    
    private InventoryDto toDto(ItemModel itemModel) {
        InventoryDto dto = new InventoryDto();
        dto.setItemCode(itemModel.getItemCode());
        dto.setItemName(itemModel.getItemName());
        dto.setItemDescription(itemModel.getItemDescription());
        dto.setInStock(itemModel.getInStock());
        return dto;
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
                if(itemModel.getInStock() < 0) throw new ItemNotFoundException("Item With ID '" + orderId + "' Doesn't Exist");
                itemRepository.save(itemModel);
            }
            else throw new ItemNotFoundException("Item With ID '" + orderId + "' Doesn't Exist");
        }
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = ItemNotFoundException.class)
    public void consumeInventoryCreated(OrderItemsDto orderItemsDto)
    {
        for(Map.Entry<UUID, Integer> entry : orderItemsDto.getItems().entrySet())
        {
            UUID orderId = entry.getKey();
            Integer quantity = entry.getValue();
            Optional<ItemModel> optionalItemModel = itemRepository.findById(orderId);
            if(optionalItemModel.isPresent())
            {
                ItemModel itemModel = optionalItemModel.get();
                itemModel.setReserved(itemModel.getReserved() - quantity);
                if(itemModel.getReserved() < 0) 
                {
                    publishToKafka(orderItemsDto.getOrderId(), Boolean.FALSE);
                    throw new ItemNotFoundException("Item With ID '" + orderId + "' Doesn't Exist");
                }
                itemRepository.save(itemModel);
            }
            else {
                publishToKafka(orderItemsDto.getOrderId(), Boolean.FALSE);
                throw new ItemNotFoundException("Item With ID '" + orderId + "' Doesn't Exist");
            }
        }
        publishToKafka(orderItemsDto.getOrderId(), Boolean.TRUE);
    }
    
    private void publishToKafka(Long orderId, Boolean creationStatus)
    {
        kafkaProducer.sendMessage("inventoryCreatedTopic", Map.of(orderId, creationStatus));
    }

    @Override
    public void failTimedOutReserves() {
        List<ItemModel> itemModels = itemRepository.findByLastModifiedBefore(LocalDateTime.now().minusMinutes(30));
        itemModels.forEach(item -> {item.setInStock(item.getInStock() + item.getReserved()); item.setReserved(0);});
        itemRepository.saveAll(itemModels);
    }
}
