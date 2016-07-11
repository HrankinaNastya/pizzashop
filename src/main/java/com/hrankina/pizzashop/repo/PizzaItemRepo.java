package com.hrankina.pizzashop.repo;

import com.hrankina.pizzashop.entities.PizzaItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * creation date 11.07.2016
 *
 * @author A.Hrankina
 */
public interface PizzaItemRepo extends PagingAndSortingRepository<PizzaItem, Long> {

    @Query("from PizzaItem p" +
            " where (p.pizza.id = :pizzaId or :pizzaId is null)" +
            " and (p.shortcake.id = :shortcakeId or :shortcakeId is null)" +
            " and (p.sauce.id = :sauceId or :sauceId is null)")
    Page<PizzaItem> findAll(Pageable pageable, @Param("pizzaId") Long pizzaId, @Param("shortcakeId") Long shortcakeId, @Param("sauceId") Long sauceId);

    @Query(" from PizzaItem p")
    List<PizzaItem> findAll();

}
