package com.hrankina.pizzashop.services;

import com.hrankina.pizzashop.entities.Role;
import com.hrankina.pizzashop.repo.RoleRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepo repo;

    public void save(Role role) {
        repo.save(role);
    }

    public Role get(Long id) {
        return repo.findOne(id);
    }

    public Page<Role> getAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public void remove(Long id) {
        repo.delete(id);
    }

    public boolean exists(Long id) {
        return repo.exists(id);
    }

}
