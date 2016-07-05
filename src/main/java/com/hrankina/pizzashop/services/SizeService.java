package com.hrankina.pizzashop.services;

import com.hrankina.pizzashop.entities.Size;
import com.hrankina.pizzashop.repo.SizeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * creation date 05.07.2016
 *
 * @author A.Hrankina
 */
@Service
public class SizeService {

    @Autowired
    private SizeRepo repo;

    public void save(Size size) {
        repo.save(size);
    }

    public Size get(Long id) {
        return repo.findOne(id);
    }

    public Page<Size> getAll(Pageable pageable) {
        return repo.findAllNonDeleted(pageable);
    }

    public void remove(Long id) {
        repo.delete(id);
    }

    public boolean exists(Long id) {
        return repo.exists(id);
    }

}