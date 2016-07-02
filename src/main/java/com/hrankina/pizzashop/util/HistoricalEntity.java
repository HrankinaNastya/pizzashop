package com.hrankina.pizzashop.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.hrankina.pizzashop.entities.User;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.Date;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
@MappedSuperclass
public abstract class HistoricalEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_ID", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CR_DATE", updatable = false)
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateTimeJSDeserializer.class)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPD_DATE")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateTimeJSDeserializer.class)
    private Date updatedAt;

    @PrePersist
    @PreUpdate
    void setUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            user = (User) principal;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonSetter("usersId")
    public void mapRoleIdFromJSON(Long roleId) {
        if (user == null) {
            user = new User();
        }
        user.setId(roleId);
    }

    @JsonIgnore
    public Long getUsersId() {
        return user != null ? user.getId() : null;
    }

    @JsonIgnore
    public String getUsersname() {
        return user != null ? user.getUsername() : null;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

}
