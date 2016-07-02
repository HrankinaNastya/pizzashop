package com.hrankina.pizzashop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import com.hrankina.pizzashop.util.HistoricalEntity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "USERS")
public class User extends HistoricalEntity implements UserDetails {

    @Column(name = "LAST_NAME", length = 50)
    private String lastName;

    @Column(name = "SECOND_NAME", length = 50)
    private String secondName;

    @Column(name = "FIRST_NAME", length = 50)
    private String firstName;

    @Column(name = "LOGIN", length = 50)
    private String username;

    @JsonIgnore
    @Column(name = "PASSWD", length = 60)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLES_ID", nullable = false)
    private Role role;

    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;

    @JsonIgnore
    @Column(name = "DELETED")
    private Boolean deleted;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName != null) {
            int size = lastName.length();
            this.lastName = lastName.substring(0, size > 50 ? 50 : size);
        } else {
            this.lastName = null;
        }
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        if (secondName != null) {
            int size = secondName.length();
            this.secondName = secondName.substring(0, size > 50 ? 50 : size);
        } else {
            this.secondName = null;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName != null) {
            int size = firstName.length();
            this.firstName = firstName.substring(0, size > 50 ? 50 : size);
        } else {
            this.firstName = null;
        }
    }

    public void setUsername(String username) {
        if (username != null) {
            int size = username.length();
            this.username = username.substring(0, size > 50 ? 50 : size);
        } else {
            this.username = null;
        }
    }

    public void setPassword(String password) {
        if (password != null) {
            int size = password.length();
            this.password = password.substring(0, size > 60 ? 60 : size);
        } else {
            this.password = null;
        }
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getRoleId() {
        return role.getId();
    }

    @JsonSetter("roleId")
    public void mapRoleIdFromJSON(Long roleId) {
        if (role == null) {
            role = new Role();
        }
        this.role.setId(roleId);
    }

    public String getRoleName() {
        return role.getName();
    }

    public String getRoleComments() {
        return role.getComments();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getShortName() {
        StringBuilder sb = new StringBuilder();
        if (lastName != null && !lastName.isEmpty()) {
            sb.append(lastName);
            if (firstName != null && !firstName.isEmpty()) {
                sb.append(" ").append(firstName.charAt(0)).append(".");
                if (secondName != null && !secondName.isEmpty()) {
                    sb.append(" ").append(secondName.charAt(0)).append(".");
                }
            } else {
                if (secondName != null && !secondName.isEmpty()) {
                    sb.append(" ").append(secondName.charAt(0)).append(".");
                }
            }
        } else {
            if (firstName != null && !firstName.isEmpty()) {
                sb.append(firstName.charAt(0)).append(".");
                if (secondName != null && !secondName.isEmpty()) {
                    sb.append(" ").append(secondName.charAt(0)).append(".");
                }
            } else {
                if (secondName != null && !secondName.isEmpty()) {
                    sb.append(secondName.charAt(0)).append(".");
                }
            }
        }
        return sb.toString();
    }

    /* SPRING SECURITY METHODS */
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonSetter("password")
    @JsonProperty("password")
    public void encodePassword(String password) { //TODO: setters length control...
        if (password != null && !password.isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.password = passwordEncoder.encode(password);
        }
    }

    @JsonIgnore
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(role.getName()));
        return list;
    }

    @JsonIgnore public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore public boolean isCredentialsNonExpired() {
        return true;
    }

}
