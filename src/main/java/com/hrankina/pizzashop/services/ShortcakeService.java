package com.hrankina.pizzashop.services;

import com.hrankina.pizzashop.entities.Shortcake;
import com.hrankina.pizzashop.repo.ShortcakeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * creation date 03.07.2016
 *
 * @author A.Hrankina
 */
@Service
public class ShortcakeService {

    @Autowired
    private ShortcakeRepo repo;

    public void save(Shortcake shortcake) {
        repo.save(shortcake);
    }

    public Shortcake get(Long id) {
        return repo.findOne(id);
    }

    public Page<Shortcake> getAll(Pageable pageable) {
        return repo.findAllNonDeleted(pageable);
    }

    public void remove(Long id) {
        repo.delete(id);
    }

    public boolean exists(Long id) {
        return repo.exists(id);
    }

}