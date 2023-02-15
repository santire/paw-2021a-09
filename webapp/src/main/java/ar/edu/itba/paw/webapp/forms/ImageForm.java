package ar.edu.itba.paw.webapp.forms;

import ar.edu.itba.paw.webapp.validators.MultipartFileSizeValid;
import ar.edu.itba.paw.webapp.validators.ValidImage;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class ImageForm {
    @ValidImage
    @MultipartFileSizeValid
    @NotNull
    private MultipartFile profileImage;

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }
}
