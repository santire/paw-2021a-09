package ar.edu.itba.paw.webapp.forms;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateRestaurantForm {
    @Size(min = 2, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ()'°´¨!&.\\s]+")
    private String name;

    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ&()'°´¨!.\\s]+")
    @Size(min = 6, max = 100)
    private String address;

    @Size(min = 8, max = 30)
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;



    @Size(max=3)
    private Integer[] tags;

    @Pattern(regexp = "(^((http|https):\\/\\/)?(www.)?(?!.*(http|https|www.))facebook+\\.com+(\\/[.\\-_a-zA-Z0-9#]+\\/?)$)|(^$)")
    private String facebook;
    @Pattern(regexp = "(^((http|https):\\/\\/)?(www.)?(?!.*(http|https|www.))instagram+\\.com+(\\/[.\\-_a-zA-Z0-9#]+\\/?)$)|(^$)")
    private String instagram;
    @Pattern(regexp = "(^((http|https):\\/\\/)?(www.)?(?!.*(http|https|www.))twitter+\\.com+(\\/[.\\-_a-zA-Z0-9#]+\\/?)$)|(^$)")
    private String twitter;


    public Integer[] getTags() {
        return tags;
    }
    public void setTags(Integer[] tags) {
        this.tags = tags;
    }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setFacebook(String facebook) { this.facebook = facebook; }
    public void setInstagram(String instagram) { this.instagram = instagram; }
    public void setTwitter(String twitter) { this.twitter = twitter; }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }

    public String getFacebook() { return facebook; }
    public String getInstagram() { return instagram; }
    public String getTwitter() { return twitter; }
}
