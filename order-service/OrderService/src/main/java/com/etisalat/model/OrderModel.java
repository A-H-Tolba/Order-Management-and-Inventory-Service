package com.etisalat.model;

import com.etisalat.ref.OrderStatusRef;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author Ahmed Tolba
 */
@Table(name = "orders")
@Entity
@Getter
@Setter
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    
    private String orderNumber;
    
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatusRef status;
    
    @OneToMany
    private Set<OderItemsModel> orItemsModels;
}
