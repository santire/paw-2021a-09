package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Comment;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;

public class CommentDto {

    private Long id;
    private String message;
    private String date;
    private String username;
    private URI self;
    private URI user;
    private URI restaurant;

    public static CommentDto fromComment(Comment comment, UriInfo uriInfo) {
        final CommentDto dto = new CommentDto();

        dto.id = comment.getId();
        dto.message = comment.getUserComment();
        dto.date = comment.getDate().toString();
        dto.username = comment.getUser().getUsername();

        dto.user = uriInfo.getBaseUriBuilder().path("users").path(comment.getUser().getId().toString()).build();
        dto.restaurant = uriInfo.getBaseUriBuilder().path("restaurants").path(comment.getRestaurant().getId().toString()).build();
        dto.self = uriInfo.getBaseUriBuilder().path("comments").path(comment.getId().toString()).build();


        return dto;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
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

    public void setMessage(String userComment) {
        this.message = userComment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate(LocalDate date) {
        this.date = date.toString();
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public URI getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(URI restaurant) {
        this.restaurant = restaurant;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
