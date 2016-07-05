package com.hrankina.pizzashop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrankina.pizzashop.util.HistoricalEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * creation date 05.07.2016
 *
 * @author A.Hrankina
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "PIZZAS")
public class Pizza extends HistoricalEntity {

    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "PRICE")
    private Double  price;

    @Column(name = "PREVIEW")
    private byte[] preview;

    @Column(name = "PREVIEWLARGE")
    private byte[] previewLarge;

    @Column(name = "DESCRIPTION")
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getPreview() {
        return preview;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public byte[] getPreviewLarge() {
        return previewLarge;
    }

    public void setPreviewLarge(byte[] previewLarge) {
        this.previewLarge = previewLarge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
