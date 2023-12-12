package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

public class MenuItemForm {

    @Size(min = 1, max = 100)
//    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ ()'´¨!.,]+")
    @NotEmpty
    private String name;

    @Size(min = 0, max = 100)
//    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ ()'´¨!.,]+|^$")
    @NotEmpty
    private String description;

    @Min(1)
    @Max(100000)
    @NotNull
    private Float price;


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
