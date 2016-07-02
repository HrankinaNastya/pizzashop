package com.hrankina.pizzashop.repo;

import com.hrankina.pizzashop.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public interface UserRepo extends PagingAndSortingRepository<User, Long> {

    @Query("from User u" +
            " where coalesce(u.deleted, 0) <> 1" +
            " and (u.username = :username)")
    User findByUsername(@Param("username") String username);

    User findFirstByRole_Id(Long roleId);

    @Query("from User u" +
            " where coalesce(u.deleted, 0) <> 1" +
            " and (u.role.id = :roleId or :roleId is null)" +
            " and (upper(u.username) like concat('%', :username, '%') or :username is null)")
    Page<User> findAllNonDeleted(Pageable pageable, @Param("roleId") Long roleId, @Param("username") String username);

}

