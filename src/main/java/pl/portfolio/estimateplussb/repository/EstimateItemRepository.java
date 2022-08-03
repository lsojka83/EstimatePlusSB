package pl.portfolio.estimateplussb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.portfolio.estimateplussb.entity.EstimateItem;

import java.util.List;

public interface EstimateItemRepository extends JpaRepository<EstimateItem, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "DELETE FROM estimateplussb.estimate_estimateitem WHERE estimate_items_id = ?1")
    void deleteFromParentRelationTableById(Long id);

    @Query(nativeQuery = true,value = "SELECT * FROM estimateplussb.estimate_estimateitem WHERE estimate_items_id = ?1")
    EstimateItem findByItemInParentTable(Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "DELETE FROM estimateplussb.estimateitem WHERE id = ?1")
    void deleteById(String id);


    @Query("SELECT ei FROM EstimateItem ei LEFT JOIN FETCH ei.priceListItem pi where pi.id = :id")
    List<EstimateItem> findAllByPriceListItemId(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM estimateplussb.estimateitem LEFT JOIN estimateplussb.estimate_estimateitem On estimateitem.id = estimate_estimateitem.estimate_items_id WHERE Estimate_id is null")
    List<EstimateItem> findAllItemsNotPresentInParentJoiningTable();
}