package ar.edu.itba.paw.model;

import javax.persistence.*;

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
    private Float price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    MenuItem() {
        //Just for hibernate
    }
    
    public MenuItem(Long id, String name, String description, Float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public MenuItem(String name, String description, Float price) {
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

    public Float getPrice() {
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

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}
