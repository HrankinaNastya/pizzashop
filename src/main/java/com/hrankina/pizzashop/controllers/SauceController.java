package com.hrankina.pizzashop.controllers;

import com.hrankina.pizzashop.entities.Sauce;
import com.hrankina.pizzashop.services.SauceService;
import com.hrankina.pizzashop.util.ErrorWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * creation date 03.07.2016
 *
 * @author A.Hrankina
 */
@RestController
@RequestMapping("/api/sauces")
public class SauceController {

    @Autowired
    private SauceService service;

    /**
     * <p>Info: Get all sauces.</p>
     * <p>Path: <b>/api/sauces</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @return page with JSONArray of sauces.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Sauce>> getList(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                              @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit,
                                              @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir,
                                              @RequestParam(value = "order", required = false, defaultValue = "id") String order) {

        Sort sort = new Sort(dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        PageRequest pageRequest = new PageRequest(page, limit, sort);

        return new ResponseEntity<>(service.getAll(pageRequest), HttpStatus.OK);
    }

    /**
     * <p>Info: Get list of sauces.</p>
     * <p>Path: <b>/api/sauces/list</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @return HttpStatus.OK with list of sauces.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<Sauce>> getSauces() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    /**
     * <p>Info: Get sauce's data by id.</p>
     * <p>Path: <b>/api/sauces/{id}</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @param id sauce's id
     * @return JSON-object with sauce's data with HttpStatus.OK or HttpStatus.NOT_FOUND if not found.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Sauce> getOne(@PathVariable("id") Long id) {
        Sauce result = service.get(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * <p>Info: Add new sauce or update existing.</p>
     * <p>Path: <b>/api/sauces</b>.</p>
     * <p>Request method: <b>POST</b>.</p>
     *
     * @param sauce JSON-object with sauce's fields
     *             <br>
     *             JSON example (adding new):
     *             <pre>
     *              {
     *              }
     *             </pre>
     * @return <b>ID</b> of saved record with HttpStatus.OK or HttpStatus.BAD_REQUEST if some required fields are missed.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addOne(@RequestBody Sauce sauce) {

        String name = sauce.getName();
        if (name == null || name.trim().isEmpty()) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Название обязательно для заполнения"), HttpStatus.BAD_REQUEST);
        }

        service.save(sauce);
        return new ResponseEntity<>(sauce.getId(), HttpStatus.OK);
    }

    /**
     * <p>Info: Delete sauce by id.</p>
     * <p>Path: <b>/api/sauces/{id}</b>.</p>
     * <p>Request method: <b>DELETE</b>.</p>
     *
     * @param id sauce's id
     * @return HttpStatus.OK if successfully deleted; HttpStatus.NOT_FOUND if sauce with current id is not found;
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
