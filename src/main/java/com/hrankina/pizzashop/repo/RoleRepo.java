package com.hrankina.pizzashop.repo;

import com.hrankina.pizzashop.entities.Role;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public interface RoleRepo extends PagingAndSortingRepository<Role, Long> {

}

