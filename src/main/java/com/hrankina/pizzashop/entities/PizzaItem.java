package com.hrankina.pizzashop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.hrankina.pizzashop.util.HistoricalEntity;

import javax.persistence.*;

/**
 * creation date 10.07.2016
 *
 * @author A.Hrankina
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "PIZZAITEMS")
public class PizzaItem extends HistoricalEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PIZZA_ID", nullable = false)
    private Pizza pizza;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SAUCE_ID", nullable = false)
    private Sauce sauce;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SHORTCAKE_ID", nullable = false)
    private Shortcake shortcake;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SIZE_ID", nullable = false)
    private Size size;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PRICE")
    private Double price;

    @JsonIgnore
    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    @JsonSetter("pizzaId")
    public void mapPizzaIdFromJSON(Long pizzaId) {
        if (pizzaId != null) {
            if (pizza == null) {
                pizza = new Pizza();
            }
            this.pizza.setId(pizzaId);
        }
    }

    public Long getPizzaId() {
        return pizza != null ? pizza.getId() : null;
    }

    public String getPizzaName() {
        return pizza != null ? pizza.getName() : "";
    }

    public Double getPizzaPrice() {
        return pizza != null ? pizza.getPrice() : null;
    }

    @JsonIgnore
    public Sauce getSauce() {
        return sauce;
    }

    public void setSauce(Sauce sauce) {
        this.sauce = sauce;
    }

    @JsonSetter("sauceId")
    public void mapSauceIdFromJSON(Long sauceId) {
        if (sauceId != null) {
            if (sauce == null) {
                sauce = new Sauce();
            }
            this.sauce.setId(sauceId);
        }
    }

    public Long getSauceId() {
        return sauce != null ? sauce.getId() : null;
    }

    public String getSauceName() {
        return sauce != null ? sauce.getName() : "";
    }

    @JsonIgnore
    public Shortcake getShortcake() {
        return shortcake;
    }

    public void setShortcake(Shortcake shortcake) {
        this.shortcake = shortcake;
    }

    @JsonSetter("shortcakeId")
    public void mapShortcakeIdFromJSON(Long shortcakeId) {
        if (shortcakeId != null) {
            if (shortcake == null) {
                shortcake = new Shortcake();
            }
            this.shortcake.setId(shortcakeId);
        }
    }

    public Long getShortcakeId() {
        return shortcake != null ? shortcake.getId() : null;
    }

    public String getShortcakeName() {
        return shortcake != null ? shortcake.getName() : "";
    }

    @JsonIgnore
    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @JsonSetter("sizeId")
    public void mapSizeIdFromJSON(Long sizeId) {
        if (sizeId != null) {
            if (size == null) {
                size = new Size();
            }
            this.size.setId(sizeId);
        }
    }

    public Long getSizeId() {
        return size != null ? size.getId() : null;
    }

    public String getSizeName() {
        return size != null ? size.getName() : "";
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
