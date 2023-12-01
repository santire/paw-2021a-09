package ar.edu.itba.paw.webapp.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterRestaurantForm {
    @NotNull
    private Long ownerId;
    @Size(min = 2, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ()'°´¨!&.\\s]+")
    private String name;

    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ&()'°´¨!.\\s]+")
    @Size(min = 6, max = 100)
    private String address;

    @Size(min = 8, max = 30)
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;


    @Size(max = 3)
    private Integer[] tags;

    @Pattern(regexp = "(^((http|https):\\/\\/)?(www.)?(?!.*(http|https|www.))facebook+\\.com+(\\/[.\\-_a-zA-Z0-9#]+\\/?)$)|(^$)")
    private String facebook;
    @Pattern(regexp = "(^((http|https):\\/\\/)?(www.)?(?!.*(http|https|www.))instagram+\\.com+(\\/[.\\-_a-zA-Z0-9#]+\\/?)$)|(^$)")
    private String instagram;
    @Pattern(regexp = "(^((http|https):\\/\\/)?(www.)?(?!.*(http|https|www.))twitter+\\.com+(\\/[.\\-_a-zA-Z0-9#]+\\/?)$)|(^$)")
    private String twitter;


    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer[] getTags() {
        return tags;
    }

    public void setTags(Integer[] tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
