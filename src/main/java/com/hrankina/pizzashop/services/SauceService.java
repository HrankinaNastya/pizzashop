package com.hrankina.pizzashop.services;

import com.hrankina.pizzashop.entities.Sauce;
import com.hrankina.pizzashop.repo.SauceRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * creation date 03.07.2016
 *
 * @author A.Hrankina
 */
@Service
public class SauceService {

    @Autowired
    private SauceRepo repo;

    public void save(Sauce sauce) {
        repo.save(sauce);
    }

    public Sauce get(Long id) {
        return repo.findOne(id);
    }

    public Page<Sauce> getAll(Pageable pageable) {
        return repo.findAllNonDeleted(pageable);
    }

    public List<Sauce> getAll() {
        return repo.findAll();
    }

    public void remove(Long id) {
        repo.delete(id);
    }

    public boolean exists(Long id) {
        return repo.exists(id);
    }

}
