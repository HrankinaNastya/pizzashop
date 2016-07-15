package com.hrankina.pizzashop.repo;

import com.hrankina.pizzashop.entities.Size;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * creation date 05.07.2016
 *
 * @author A.Hrankina
 */
public interface SizeRepo extends PagingAndSortingRepository<Size, Long> {

    @Query("from Size s" +
            " where coalesce(s.deleted, 0) <> 1")
    Page<Size> findAllNonDeleted(Pageable pageable);

    @Query(" from Size s")
    List<Size> findAll();
}
