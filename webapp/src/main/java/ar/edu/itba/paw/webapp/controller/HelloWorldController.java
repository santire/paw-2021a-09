package ar.edu.itba.paw.webapp.controller;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/hello")
public class HelloWorldController {

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response hello() {
//        List<MessageDTO> hello = Arrays.asList("Hello", "World!").stream().map(MessageDTO::fromString).collect(Collectors.toList());
//        return Response.ok(new GenericEntity<List<MessageDTO>>(hello) {}).build();
        List<String> hello = Arrays.asList("Hello", "World!");
        return Response.ok(hello).build();
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