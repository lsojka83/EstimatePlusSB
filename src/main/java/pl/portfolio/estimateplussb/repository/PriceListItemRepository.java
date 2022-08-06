package pl.portfolio.estimateplussb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.portfolio.estimateplussb.entity.PriceListItem;

import java.util.List;
import java.util.Optional;

public interface PriceListItemRepository extends JpaRepository<PriceListItem, Long> {

    Optional<PriceListItem> findByReferenceNumber(String refNumber);

    @Query(nativeQuery = true,
            value = "SELECT * FROM heroku_266825118c34932.pricelistitem LEFT JOIN heroku_266825118c34932.pricelist_pricelistitem on pricelistitem.id = price_list_items_id left join heroku_266825118c34932.user on price_list_id = user_price_list_id where (user.user_price_list_id is null OR user.id = ?1) AND pricelistitem.reference_number LIKE ?2")
    Optional<PriceListItem> findByUserIdAndReferenceNumber(Long userId, String refNo);

    @Query(nativeQuery = true,
            value = "SELECT * FROM heroku_266825118c34932.pricelistitem LEFT JOIN heroku_266825118c34932.pricelist_pricelistitem on pricelistitem.id = price_list_items_id left join heroku_266825118c34932.user on price_list_id = user_price_list_id where (user.user_price_list_id is null OR user.id = ?1) AND pricelistitem.reference_number LIKE ?2")
    List<PriceListItem> findAllByUserIdAndReferenceNumber(Long userId, String refNo);

}

