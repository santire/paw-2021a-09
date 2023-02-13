package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Comment;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;


import java.net.URI;

public class CommentDto {

    private Long id;
    private String userComment;
    private String date;
    private String username;

    private URI user;
    private URI restaurant;

    public static CommentDto fromComment(Comment comment, UriInfo uriInfo){
        final CommentDto dto = new CommentDto();

        dto.id = comment.getId();
        dto.userComment = comment.getUserComment();
        dto.date = comment.getDate().toString();
        dto.username = comment.getUser().getUsername();

        dto.user = uriInfo.getBaseUriBuilder().path("users/"+ comment.getUser().getId()).build();
        dto.restaurant = uriInfo.getBaseUriBuilder().path("restaurants/"+comment.getRestaurant().getId()).build();


        return  dto;
    }

    public Long getId() { return id; }

    public String getUserComment() { return userComment; }

    public String getDate() { return date; }

    public URI getUser() { return user; }

    public URI getRestaurant() { return restaurant; }

    public String getUsername() { return username; }


    public void setId(Long id) { this.id = id; }

    public void setUserComment(String userComment) { this.userComment = userComment; }

    public void setDate(LocalDate date) { this.date = date.toString(); }

    public void setUser(URI user) { this.user = user; }

    public void setRestaurant(URI restaurant) { this.restaurant = restaurant; }

    public void setUsername(String username) { this.username = username; }

}
