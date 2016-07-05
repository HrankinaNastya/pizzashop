package com.hrankina.pizzashop.repo;

import com.hrankina.pizzashop.entities.Pizza;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * creation date 05.07.2016
 *
 * @author A.Hrankina
 */
public interface PizzaRepo extends PagingAndSortingRepository<Pizza, Long> {

    @Query("from Pizza p" +
            " where coalesce(p.deleted, 0) <> 1")
    Page<Pizza> findAllNonDeleted(Pageable pageable);

}