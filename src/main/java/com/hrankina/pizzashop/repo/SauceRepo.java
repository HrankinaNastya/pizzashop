package com.hrankina.pizzashop.repo;

import com.hrankina.pizzashop.entities.Sauce;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * creation date 03.07.2016
 *
 * @author A.Hrankina
 */
public interface SauceRepo extends PagingAndSortingRepository<Sauce, Long> {

    @Query("from Sauce s" +
            " where coalesce(s.deleted, 0) <> 1")
    Page<Sauce> findAllNonDeleted(Pageable pageable);

    @Query(" from Sauce s")
    List<Sauce> findAll();
}