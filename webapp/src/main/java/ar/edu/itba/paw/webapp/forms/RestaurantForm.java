package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import ar.edu.itba.paw.webapp.validators.MultipartFileSizeValid;
import ar.edu.itba.paw.webapp.validators.ValidImage;

public class RestaurantForm {
    @Size(min = 2, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ()'°´¨!&.\\s]+")
    private String name;

    @Pattern(regexp = "[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ&()'°´¨!.\\s]+")
    @Size(min = 6, max = 100)
    private String address;

    @Size(min = 8, max = 30)
    @Pattern(regexp = "[0-9]+")
    private String phoneNumber;

    @ValidImage
    @MultipartFileSizeValid
    private MultipartFile profileImage;

    @Size(max=3)
    private Integer[] tags;

    public Integer[] getTags() {
        return tags;
    }
    public void setTags(Integer[] tags) {
        this.tags = tags;
    }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setProfileImage(MultipartFile profileImage) { this.profileImage = profileImage; }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public MultipartFile getProfileImage() { return profileImage; }
}
