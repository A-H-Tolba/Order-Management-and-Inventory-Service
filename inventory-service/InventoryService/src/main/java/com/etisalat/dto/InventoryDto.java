package com.etisalat.dto;

import jakarta.persistence.Column;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Ahmed Tolba
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {
    private UUID itemCode;
    private String itemName;
    private String itemDescription;
    private Integer inStock;
}
