package com.etisalat.dto;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Ahmed Tolba
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsDto {
    Long orderId;
    
    Map<UUID, Integer> items;
}
