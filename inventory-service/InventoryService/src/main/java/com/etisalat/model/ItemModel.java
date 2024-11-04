package com.etisalat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Ahmed Tolba
 */
@Table(name = "order_items")
@Entity
@Getter
@Setter
public class ItemModel {
    @Id
    @Column(nullable = false)
    private UUID itemCode;
    
    @Column
    private String itemName;
    
    @Column
    private String itemDescription;
    
    @Column
    private Integer inStock;
    
    @Column
    private Integer reserved;
    
    @Column
    private LocalDateTime lastModified;
}
