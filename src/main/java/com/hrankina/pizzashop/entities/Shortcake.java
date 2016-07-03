package com.hrankina.pizzashop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.hrankina.pizzashop.util.HistoricalEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * creation date 03.07.2016
 *
 * @author A.Hrankina
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "SHORTCAKES")
public class Shortcake extends HistoricalEntity {

    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "DESCRIPTION", length = 150)
    private String description;

    @JsonIgnore
    @Column(name = "DELETED")
    private Boolean deleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            int size = name.length();
            this.name = name.substring(0, size > 50 ? 50 : size);
        } else {
            this.name = null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            int size = description.length();
            this.description = description.substring(0, size > 150 ? 150 : size);
        } else {
            this.description = null;
        }
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
