package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.Restaurant;

import javax.ws.rs.core.UriInfo;

public class MenuItemDto {


    private  Long id;
    private String name;
    private String description;
    private float price;

    public static MenuItemDto fromMenuItem(MenuItem menuItem){
        final MenuItemDto dto = new MenuItemDto();

        dto.id = menuItem.getId();
        dto.name = menuItem.getName();
        dto.description = menuItem.getDescription();
        dto.price = dto.price;

        return  dto;
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

    public void setId(Long id) {
        this.id = id;
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
}
