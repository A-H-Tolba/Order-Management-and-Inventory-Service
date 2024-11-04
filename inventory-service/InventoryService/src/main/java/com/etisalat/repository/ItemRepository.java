package com.etisalat.repository;

import com.etisalat.model.ItemModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ahmed Tolba
 */
public interface ItemRepository extends JpaRepository<ItemModel, UUID>{
    
}
