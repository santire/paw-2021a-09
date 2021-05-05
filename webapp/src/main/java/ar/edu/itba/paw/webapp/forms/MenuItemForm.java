package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MenuItemForm {

    private long id;

    @Size(min = 4, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ ()'´¨!.,]+")
    private String name;

    @Size(min = 0, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ ()'´¨!.,]+|^$")
    private String description;

    @Min(0)
    @Max(100000)
    private float price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
