package com.hrankina.pizzashop.controllers;

import com.hrankina.pizzashop.entities.Shortcake;
import com.hrankina.pizzashop.services.ShortcakeService;
import com.hrankina.pizzashop.util.ErrorWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * creation date 03.07.2016
 *
 * @author A.Hrankina
 */
@RestController
@RequestMapping("/api/shortcakes")
public class ShortcakeController {

    @Autowired
    private ShortcakeService service;

    /**
     * <p>Info: Get all shortcakes.</p>
     * <p>Path: <b>/api/shortcakes</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @return page with JSONArray of shortcakes.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Shortcake>> getList(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                               @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit,
                                               @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir,
                                               @RequestParam(value = "order", required = false, defaultValue = "id") String order) {

        Sort sort = new Sort(dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        PageRequest pageRequest = new PageRequest(page, limit, sort);

        return new ResponseEntity<>(service.getAll(pageRequest), HttpStatus.OK);
    }

    /**
     * <p>Info: Get shortcake's data by id.</p>
     * <p>Path: <b>/api/shortcakes/{id}</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @param id shortcake's id
     * @return JSON-object with shortcake's data with HttpStatus.OK or HttpStatus.NOT_FOUND if not found.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Shortcake> getOne(@PathVariable("id") Long id) {
        Shortcake result = service.get(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * <p>Info: Add new shortcake or update existing.</p>
     * <p>Path: <b>/api/shortcakes</b>.</p>
     * <p>Request method: <b>POST</b>.</p>
     *
     * @param shortcake JSON-object with shortcake's fields
     *             <br>
     *             JSON example (adding new):
     *             <pre>
     *              {
     *              }
     *             </pre>
     * @return <b>ID</b> of saved record with HttpStatus.OK or HttpStatus.BAD_REQUEST if some required fields are missed.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addOne(@RequestBody Shortcake shortcake) {

        String name = shortcake.getName();
        if (name == null || name.trim().isEmpty()) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Название обязательно для заполнения"), HttpStatus.BAD_REQUEST);
        }

        service.save(shortcake);
        return new ResponseEntity<>(shortcake.getId(), HttpStatus.OK);
    }

    /**
     * <p>Info: Delete shortcake by id.</p>
     * <p>Path: <b>/api/shortcakes/{id}</b>.</p>
     * <p>Request method: <b>DELETE</b>.</p>
     *
     * @param id shortcake's id
     * @return HttpStatus.OK if successfully deleted; HttpStatus.NOT_FOUND if shortcake with current id is not found;
     * HttpStatus.BAD_REQUEST - if current record deleting forbidden.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteItem(@PathVariable("id") Long id) {
        if (!service.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        service.remove(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}