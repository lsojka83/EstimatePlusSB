package pl.portfolio.estimateplussb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.portfolio.estimateplussb.entity.PriceList;

import java.util.List;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    @Query("SELECT p FROM PriceList p LEFT JOIN FETCH p.priceListItems WHERE p.name = :name")
    PriceList findByName(@Param("name") String name);

    @Query("SELECT p FROM PriceList p LEFT JOIN FETCH p.priceListItems WHERE p.id = :id")
    PriceList findByIdWithPriceListItems(@Param("id") Long id);

//    @Query(nativeQuery = true,value = "SELECT * FROM estimateplussb.pricelist p LEFT JOIN estimateplussb.user u ON p.id=u.user_price_list_id WHERE (u.id is null OR u.id = ?1)")
    @Query(nativeQuery = true,value = "SELECT * FROM heroku_266825118c34932.pricelist p LEFT JOIN heroku_266825118c34932.user u ON p.id=u.user_price_list_id WHERE (u.id is null OR u.id = ?1)")
    List<PriceList> findAllByUserAndAllGeneral(Long id);


}
