package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.MenuItem;

public class MenuItemDto {


    private  Long id;
    private String name;
    private String description;
    private Float price;

    public static MenuItemDto fromMenuItem(MenuItem menuItem){
        final MenuItemDto dto = new MenuItemDto();

        dto.id = menuItem.getId();
        dto.name = menuItem.getName();
        dto.description = menuItem.getDescription();
        dto.price = menuItem.getPrice();

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

    public Float getPrice() {
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

    public void setPrice(Float price) {
        this.price = price;
    }
}
