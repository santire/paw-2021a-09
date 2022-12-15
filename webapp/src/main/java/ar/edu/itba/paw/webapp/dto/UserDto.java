package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.UriInfo;
import java.net.URI;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private String url;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public static UserDto fromUser(User user, String url){
        final UserDto dto = new UserDto();

        dto.userId = user.getId();
        dto.username = user.getUsername();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.email= user.getEmail();
        dto.phone =user.getPhone();

        dto.url = url;

        // Warning! Never return Password to user!
        return dto;
    }

    public static UserDto fromUser(User user){
        final UserDto dto = new UserDto();
        dto.userId = user.getId();
        dto.username = user.getUsername();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.email= user.getEmail();
        dto.phone =user.getPhone();

        // Warning! Never return Password to user!
        return dto;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUrl() {return url;}

    public String getPassword() {
        return password;
    }

    public String getEmail() {return email;}

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getPhone() {return phone;}

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public void setEmail(String email) { this.email = email; }

    public void setPhone(String phone) { this.phone = phone; }
}
