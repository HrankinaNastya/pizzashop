package com.hrankina.pizzashop.controllers;

import com.hrankina.pizzashop.entities.Pizza;
import com.hrankina.pizzashop.services.PizzaService;
import com.hrankina.pizzashop.util.ErrorWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * creation date 05.07.2016
 *
 * @author A.Hrankina
 */
@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    @Autowired
    private PizzaService service;

    /**
     * <p>Info: Get all pizzas.</p>
     * <p>Path: <b>/api/pizzas</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @return page with JSONArray of pizzas.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Pizza>> getList(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                               @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit,
                                               @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir,
                                               @RequestParam(value = "order", required = false, defaultValue = "id") String order) {

        Sort sort = new Sort(dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        PageRequest pageRequest = new PageRequest(page, limit, sort);

        return new ResponseEntity<>(service.getAll(pageRequest), HttpStatus.OK);
    }

    /**
     * <p>Info: Get pizza's data by id.</p>
     * <p>Path: <b>/api/pizzas/{id}</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @param id pizza's id
     * @return JSON-object with pizza's data with HttpStatus.OK or HttpStatus.NOT_FOUND if not found.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Pizza> getOne(@PathVariable("id") Long id) {
        Pizza result = service.get(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * <p>Info: Add new pizza or update existing.</p>
     * <p>Path: <b>/api/pizzas</b>.</p>
     * <p>Request method: <b>POST</b>.</p>
     *
     * @param pizza JSON-object with pizza's fields
     *             <br>
     *             JSON example (adding new):
     *             <pre>
     *              {
     *              }
     *             </pre>
     * @return <b>ID</b> of saved record with HttpStatus.OK or HttpStatus.BAD_REQUEST if some required fields are missed.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addOne(@RequestBody Pizza pizza) {

        String name = pizza.getName();
        if (name == null || name.trim().isEmpty()) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Название обязательно для заполнения"), HttpStatus.BAD_REQUEST);
        }

        service.save(pizza);
        return new ResponseEntity<>(pizza.getId(), HttpStatus.OK);
    }

    /**
     * <p>Info: Delete pizza by id.</p>
     * <p>Path: <b>/api/pizzas/{id}</b>.</p>
     * <p>Request method: <b>DELETE</b>.</p>
     *
     * @param id pizza's id
     * @return HttpStatus.OK if successfully deleted; HttpStatus.NOT_FOUND if pizza with current id is not found;
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
