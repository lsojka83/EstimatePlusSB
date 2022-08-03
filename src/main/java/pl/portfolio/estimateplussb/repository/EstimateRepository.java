package pl.portfolio.estimateplussb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.portfolio.estimateplussb.entity.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    @Query(value = "SELECT * FROM estimateplussb.estimate e " +
            "JOIN  estimateplussb.user_estimate ue ON e.id = ue.estimates_id " +
            "JOIN estimateplussb.user u ON ue.User_id = u.id " +
            "WHERE e.name = ?1 AND u.user_name = ?2",nativeQuery = true)
    Estimate findByNameAndUserName(String estimateName, String userName);
}
