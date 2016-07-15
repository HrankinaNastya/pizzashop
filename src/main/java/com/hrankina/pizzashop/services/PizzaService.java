package com.hrankina.pizzashop.services;

import com.hrankina.pizzashop.entities.Pizza;
import com.hrankina.pizzashop.repo.PizzaRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * creation date 05.07.2016
 *
 * @author A.Hrankina
 */
@Service
public class PizzaService {

    @Autowired
    private PizzaRepo repo;

    public void save(Pizza pizza) {
        repo.save(pizza);
    }

    public Pizza get(Long id) {
        return repo.findOne(id);
    }

    public Page<Pizza> getAll(Pageable pageable) {
        return repo.findAllNonDeleted(pageable);
    }

    public List<Pizza> getAll() {
        return repo.findAll();
    }

    public void remove(Long id) {
        repo.delete(id);
    }

    public boolean exists(Long id) {
        return repo.exists(id);
    }

}
