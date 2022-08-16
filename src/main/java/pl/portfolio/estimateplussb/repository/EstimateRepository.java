package pl.portfolio.estimateplussb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.portfolio.estimateplussb.entity.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    @Query(value = "SELECT * FROM heroku_266825118c34932.estimate e " +
            "JOIN  heroku_266825118c34932.user_estimate ue ON e.id = ue.estimates_id " +
            "JOIN heroku_266825118c34932.user u ON ue.User_id = u.id " +
            "WHERE e.name = ?1 AND u.id = ?2",nativeQuery = true)
    Estimate findByNameAndUserId(String estimateName, Long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "DELETE FROM heroku_266825118c34932.estimate_estimateitem WHERE estimate_items_id = ?1")
    void deleteFromParentRelationTableById(Long id);
}
