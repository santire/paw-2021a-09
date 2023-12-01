package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.MenuItem;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class MenuItemDto {


    private Long id;
    private String name;
    private String description;
    private Float price;
    private URI self;
    private URI restaurant;

    public static MenuItemDto fromMenuItem(MenuItem menuItem, UriInfo uriInfo) {
        final MenuItemDto dto = new MenuItemDto();

        dto.id = menuItem.getId();
        dto.name = menuItem.getName();
        dto.description = menuItem.getDescription();
        dto.price = menuItem.getPrice();

        dto.self = uriInfo.getBaseUriBuilder()
                .path("restaurants")
                .path(menuItem.getRestaurant().getId().toString())
                .path("menu")
                .path(menuItem.getId().toString()).build();

        dto.restaurant = uriInfo.getBaseUriBuilder()
                .path("restaurants")
                .path(menuItem.getRestaurant().getId().toString())
                .build();

        return dto;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(URI restaurant) {
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
