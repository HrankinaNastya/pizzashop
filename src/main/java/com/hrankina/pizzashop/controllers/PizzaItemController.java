package com.hrankina.pizzashop.controllers;

import com.hrankina.pizzashop.entities.PizzaItem;
import com.hrankina.pizzashop.services.PizzaItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * creation date 11.07.2016
 *
 * @author A.Hrankina
 */
@RestController
@RequestMapping("/api/pizza-items")
public class PizzaItemController {

    @Autowired
    private PizzaItemService service;

    /**
     * <p>Info: Get all pizza items.</p>
     * <p>Path: <b>/api/pizza-items</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @return page with JSONArray of pizza items.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<PizzaItem>> getList(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                 @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit,
                                                 @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir,
                                                 @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                                 @RequestParam(value = "pizza_id", required = false) Long pizzaId,
                                                 @RequestParam(value = "shortcake_id", required = false) Long shortcakeId,
                                                 @RequestParam(value = "sauce_id", required = false) Long sauceId,
                                                 @RequestParam(value = "size_id", required = false) Long sizeId) {

        Sort sort = new Sort(dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        PageRequest pageRequest = new PageRequest(page, limit, sort);

        return new ResponseEntity<>(service.getAll(pageRequest, pizzaId, shortcakeId, sauceId, sizeId), HttpStatus.OK);
    }

    /**
     * <p>Info: Get pizza item's data by id.</p>
     * <p>Path: <b>/api/pizza-items/{id}</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @param id pizza item's id
     * @return JSON-object with pizza item's data with HttpStatus.OK or HttpStatus.NOT_FOUND if not found.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PizzaItem> getOne(@PathVariable("id") Long id) {
        PizzaItem result = service.get(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * <p>Info: Add new pizza item or update existing.</p>
     * <p>Path: <b>/api/pizza-items</b>.</p>
     * <p>Request method: <b>POST</b>.</p>
     *
     * @param pizzaItem JSON-object with pizza item's fields
     *             <br>
     *             JSON example (adding new):
     *             <pre>
     *              {
     *              }
     *             </pre>
     * @return <b>ID</b> of saved record with HttpStatus.OK or HttpStatus.BAD_REQUEST if some required fields are missed.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addOne(@RequestBody PizzaItem pizzaItem) {

        service.save(pizzaItem);
        return new ResponseEntity<>(pizzaItem.getId(), HttpStatus.OK);
    }

    /**
     * <p>Info: Delete pizza item by id.</p>
     * <p>Path: <b>/api/pizza-items/{id}</b>.</p>
     * <p>Request method: <b>DELETE</b>.</p>
     *
     * @param id pizza item's id
     * @return HttpStatus.OK if successfully deleted; HttpStatus.NOT_FOUND if pizza item with current id is not found;
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
