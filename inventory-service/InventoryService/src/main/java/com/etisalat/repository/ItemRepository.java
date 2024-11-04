package com.etisalat.repository;

import com.etisalat.model.ItemModel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ahmed Tolba
 */
public interface ItemRepository extends JpaRepository<ItemModel, UUID>{
    List<ItemModel> findByLastModifiedBefore(LocalDateTime threshodTime);
}
