package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_comment_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "comments_comment_id_seq", name = "comments_comment_id_seq")
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column
    private String userComment;

    Comment(){
        // Just for hibernate
    }

    public Comment(Long id, User user, Restaurant restaurant, String userComment){
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.userComment = userComment;
    }

    public Comment(String userComment){
        this.userComment = userComment;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Restaurant getRestaurant() { return restaurant; }

    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public String getUserComment() { return userComment; }

    public void setUserComment(String userComment) { this.userComment = userComment; }

}
