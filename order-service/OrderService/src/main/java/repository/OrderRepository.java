package repository;

import com.etisalat.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ahmed Tolba
 */
public interface OrderRepository extends JpaRepository<OrderModel, Long>{
    Page<OrderModel> findByUser_UserName(String username, Pageable pageable);
}