package com.hrankina.pizzashop.services;

import com.hrankina.pizzashop.entities.User;
import com.hrankina.pizzashop.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
@Service
public class UserService implements UserDetailsService {

    private static final String ANONYMOUS_PRINCIPAL_NAME = "anonymousUser";

    @Autowired
    private UserRepo repo;

    public void save(User user) {
        user.setDeleted(Boolean.FALSE);
        try {
            user.isEnabled();
        } catch (NullPointerException npe) {
            user.setEnabled(Boolean.FALSE);
        }
        repo.save(user);
    }

    public User get(Long id) {
        return repo.findOne(id);
    }

    public Page<User> getAll(Pageable pageable, Long roleId, String username) {
        return repo.findAllNonDeleted(pageable, roleId, username == null || username.isEmpty() ? null : username.toUpperCase());
    }

    public void remove(Long id) {
        User user = repo.findOne(id);
        user.setDeleted(Boolean.TRUE);
        repo.save(user);
    }

    public boolean exists(Long id) {
        return repo.exists(id);
    }

    public User getUserByName(String name) {
        return repo.findByUsername(name);
    }

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userDetails = repo.findByUsername(s);
        if (userDetails == null || userDetails.isDeleted())  {
            throw new UsernameNotFoundException(null);
        }
        return userDetails;
    }

    public boolean isUniqueName(String name) {
        return repo.findByUsername(name) == null;
    }

    public User getCurrentUser() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isAuthorized() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(ANONYMOUS_PRINCIPAL_NAME);
    }

    public boolean existsByRoleId(Long roleId) {
        return repo.findFirstByRole_Id(roleId) != null;
    }

}
