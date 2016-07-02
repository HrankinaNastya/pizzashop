package com.hrankina.pizzashop.controllers;

import com.hrankina.pizzashop.entities.Role;
import com.hrankina.pizzashop.services.RoleService;
import com.hrankina.pizzashop.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService service;
    @Autowired
    private UserService userService;

    /**
     * <p>Info: Get all roles.</p>
     * <p>Path: <b>/api/roles</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @return page with JSONArray of roles.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Role>> getList(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                              @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit,
                                              @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir,
                                              @RequestParam(value = "order", required = false, defaultValue = "id") String order) {

        Sort sort = new Sort(dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        PageRequest pageRequest = new PageRequest(page, limit, sort);

        return new ResponseEntity<>(service.getAll(pageRequest), HttpStatus.OK);
    }

    /**
     * <p>Info: Get role's data by id.</p>
     * <p>Path: <b>/api/roles/{id}</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @param id role's id
     * @return JSON-object with role's data with HttpStatus.OK or HttpStatus.NOT_FOUND if not found.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Role> getOne(@PathVariable("id") Long id) {
        Role result = service.get(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * <p>Info: Add new role or update existing.</p>
     * <p>Path: <b>/api/roles</b>.</p>
     * <p>Request method: <b>POST</b>.</p>
     *
     * @param role JSON-object with role's fields
     *             <br>
     *             JSON example (adding new):
     *             <pre>
     *              {
     *              }
     *             </pre>
     * @return <b>ID</b> of saved record with HttpStatus.OK or HttpStatus.BAD_REQUEST if some required fields are missed.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Long> addOne(@RequestBody Role role) {
        service.save(role);
        return new ResponseEntity<>(role.getId(), HttpStatus.OK);
    }

    /**
     * <p>Info: Delete role by id.</p>
     * <p>Path: <b>/api/roles/{id}</b>.</p>
     * <p>Request method: <b>DELETE</b>.</p>
     *
     * @param id role's id
     * @return HttpStatus.OK if successfully deleted; HttpStatus.NOT_FOUND if role with current id is not found;
     * HttpStatus.BAD_REQUEST - if current record deleting forbidden.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteItem(@PathVariable("id") Long id) {
        if (!service.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //TODO: check 2 service layer
        if (userService.existsByRoleId(id)) {
            return new ResponseEntity<>("Current entity has linked records.", HttpStatus.BAD_REQUEST);
        }
        service.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
