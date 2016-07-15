package com.hrankina.pizzashop.repo;

import com.hrankina.pizzashop.entities.Shortcake;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * creation date 03.07.2016
 *
 * @author A.Hrankina
 */
public interface ShortcakeRepo extends PagingAndSortingRepository<Shortcake, Long> {

    @Query("from Shortcake s" +
            " where coalesce(s.deleted, 0) <> 1")
    Page<Shortcake> findAllNonDeleted(Pageable pageable);

    @Query(" from Shortcake s")
    List<Shortcake> findAll();

}