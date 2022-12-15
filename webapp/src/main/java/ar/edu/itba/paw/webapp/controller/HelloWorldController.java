package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/hello")
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response hello() {
        List<String> hello = Arrays.asList("Hello", "World!");
        return Response.ok(hello).build();
    }

    @GET
    @Path("/me")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response helloUser(@Context HttpServletRequest request) {
//        Optional<User> user = getLoggedUser(request);
//        List<String> hello = Arrays.asList("Hello", " World!");
//        if (user.isPresent()){
//            hello = Arrays.asList("Hello", user.get().getFirstName());
//        }
//        return Response.ok(hello).build();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return Response.ok(currentPrincipalName).build();
    }

    @ModelAttribute("loggedUser")
    public Optional<User> getLoggedUser(HttpServletRequest request){
        return userService.findByUsername(request.getRemoteUser());
    }



}
 class MessageDTO {
    private Long id;
    private String message;

    public static MessageDTO fromString(String message) {
        final MessageDTO dto = new MessageDTO();
        dto.id = null;
        dto.message = message;
        return dto;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}