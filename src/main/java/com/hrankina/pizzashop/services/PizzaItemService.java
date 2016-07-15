package com.hrankina.pizzashop.services;

import com.hrankina.pizzashop.entities.PizzaItem;
import com.hrankina.pizzashop.repo.PizzaItemRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * creation date 11.07.2016
 *
 * @author A.Hrankina
 */
@Service
public class PizzaItemService {

    @Autowired
    private PizzaItemRepo repo;

    public void save(PizzaItem pizzaItem) {
        repo.save(pizzaItem);
    }

    public PizzaItem get(Long id) {
        return repo.findOne(id);
    }

    public Page<PizzaItem> getAll(Pageable pageable, Long pizzaId, Long shortcakeId, Long sauceId, Long sizeId) {
        return repo.findAll(pageable, pizzaId, shortcakeId, sauceId, sizeId);
    }

    public List<PizzaItem> getAll() {
        return repo.findAll();
    }

    public void remove(Long id) {
        repo.delete(id);
    }

    public boolean exists(Long id) {
        return repo.exists(id);
    }

}