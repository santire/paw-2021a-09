package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Tags;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Component
@Path("/tags")
public class TagsController {
    @GET
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response getRestaurantsTags() {
        return Response.ok(new GenericEntity<Collection<Tags>>(Tags.allTags().values()){}).build();
    }
}
