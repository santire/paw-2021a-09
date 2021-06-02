package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "menu_items")
public class MenuItem {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_items_menu_item_id_seq")
    @SequenceGenerator(sequenceName = "menu_items_menu_item_id_seq", name = "menu_items_menu_item_id_seq", allocationSize = 1)
    @Column(name = "menu_item_id")
    private  Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 300)
    private String description;

    @Column
    private float price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    MenuItem() {
        //Just for hibernate
    }

    public MenuItem(Long id, String name, String description, float price, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
    }
    
    public MenuItem(Long id, String name, String description, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public MenuItem(String name, String description, float price) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}
