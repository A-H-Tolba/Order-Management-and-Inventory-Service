package com.etisalat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class OderItemsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    
    private UUID itemCode;
    
    private Integer quantity;
}
