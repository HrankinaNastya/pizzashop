package com.hrankina.pizzashop.controllers;

import com.hrankina.pizzashop.entities.Role;
import com.hrankina.pizzashop.entities.User;
import com.hrankina.pizzashop.services.RoleService;
import com.hrankina.pizzashop.services.UserService;
import com.hrankina.pizzashop.util.ErrorWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private RoleService roleService;

    /**
     * <p>Info: Get all users.</p>
     * <p>Path: <b>/api/users</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @return page with JSONArray of users.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<User>> getList(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                              @RequestParam(value = "limit", required = false, defaultValue = "30") Integer limit,
                                              @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir,
                                              @RequestParam(value = "order", required = false, defaultValue = "id") String order,
                                              @RequestParam(value = "role_id", required = false) Long roleId,
                                              @RequestParam(value = "username", required = false) String username) {

        Sort sort = new Sort(dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, order);
        PageRequest pageRequest = new PageRequest(page, limit, sort);

        return new ResponseEntity<>(service.getAll(pageRequest, roleId, username), HttpStatus.OK);
    }

    /**
     * <p>Info: Get user's data by id.</p>
     * <p>Path: <b>/api/users/{id}</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @param id user's id
     * @return JSON-object with user's data with HttpStatus.OK or HttpStatus.NOT_FOUND if not found.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
        User result = service.get(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * <p>Info: Add new user or update existing.</p>
     * <p>Path: <b>/api/users</b>.</p>
     * <p>Request method: <b>POST</b>.</p>
     *
     * @param user JSON-object with user's fields
     *             <br>
     *             JSON example (adding new):
     *             <pre>
     *              {
     *              }
     *             </pre>
     * @return <b>ID</b> of saved record with HttpStatus.OK or HttpStatus.BAD_REQUEST if some required fields are missed.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> addOne(@RequestBody User user) {
        if (user.getUsername() == null) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Логин обязателен для заполнения"), HttpStatus.BAD_REQUEST);
        }

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        if ((firstName == null || firstName.trim().isEmpty()) || (lastName == null || lastName.trim().isEmpty())) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Имя и фамилия обязательны для заполнения"), HttpStatus.BAD_REQUEST);
        }

        user.setUsername(user.getUsername().toLowerCase());
        String name = user.getUsername();
        Long id = user.getId();

        Role role = user.getRole();
        if (role == null) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Роль обязательна для заполнения"), HttpStatus.BAD_REQUEST);
        } else if (roleService.get(role.getId()) == null) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Указанной роли не существует"), HttpStatus.BAD_REQUEST);
        }

        if (isNotUniqueName(id, name)) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Такой логин уже существует в системе"), HttpStatus.BAD_REQUEST);
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            if (id == null) {
                return new ResponseEntity<>(ErrorWrapper.wrap("При создании пароль обязателен для заполнения"), HttpStatus.BAD_REQUEST);
            } else {
                user.setPassword(service.get(id).getPassword());
            }
        }

        service.save(user);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    /**
     * <p>Info: Delete user by id.</p>
     * <p>Path: <b>/api/users/{id}</b>.</p>
     * <p>Request method: <b>DELETE</b>.</p>
     *
     * @param id user's id
     * @return HttpStatus.OK if successfully deleted or HttpStatus.NOT_FOUND if user with current id is not found.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteItem(@PathVariable("id") Long id) {
        if (!service.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        if (id.equals(user.getId())) {
            return new ResponseEntity<>(ErrorWrapper.wrap("Нельзя удалить текущего пользователя!"), HttpStatus.BAD_REQUEST);
        }
        service.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * <p>Info: Check if current username is existing.</p>
     * <p>Path: <b>/api/check/{username}</b>.</p>
     * <p>Request method: <b>GET</b>.</p>
     *
     * @param username - name of user.
     * @return HttpStatus.FOUND if current username already defined or HttpStatus.NOT_FOUND if not.
     */
    @RequestMapping(value = "check/{username}", method = RequestMethod.GET)
    public ResponseEntity<String> isUniqueName(@PathVariable("username") String username) {
        return new ResponseEntity<>(service.isUniqueName(username) ? HttpStatus.NOT_FOUND : HttpStatus.FOUND);
    }

    private boolean isNotUniqueName(Long id, String name) {
        User storedUser = service.getUserByName(name);
        return storedUser != null && !storedUser.getId().equals(id);
    }

}
