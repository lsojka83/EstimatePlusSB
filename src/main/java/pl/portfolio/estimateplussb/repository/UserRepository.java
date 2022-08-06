package pl.portfolio.estimateplussb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.portfolio.estimateplussb.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.estimates WHERE u.userName = :name")
    User findByUserName(@Param("name") String name);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.estimates WHERE u.id = :id")
    User findByIdWithEstimates(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userPriceList WHERE u.id = :id")
    User findByIdWithPricelist(@Param("id") Long id);

    Optional<User> findByUuid(String uuid);


}
