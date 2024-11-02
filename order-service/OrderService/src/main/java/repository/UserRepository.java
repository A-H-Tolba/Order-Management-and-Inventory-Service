package repository;

import com.etisalat.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Ahmed Tolba
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{
    UserModel findByUserName(String username);
}
