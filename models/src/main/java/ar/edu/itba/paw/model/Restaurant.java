package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurants_restaurant_id_seq")
    @SequenceGenerator(sequenceName = "restaurants_restaurant_id_seq", name = "restaurants_restaurant_id_seq", allocationSize = 1)
    @Column(name = "restaurant_id")
    private Long id;


    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String address;

    @Column(name = "phone_number", length = 100)
    private String phoneNumber;

    @Column
    private float rating;
    

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User owner;

    // @Formula("(SELECT COUNT(l.like_id) from likes l where l.restaurant_id = id)")
    // @Transient
    // private int likes;

    @ElementCollection(targetClass = Tags.class)
    @CollectionTable(name = "restaurant_tags",
            joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "tag_id")
    private List<Tags> tags;

    @OneToMany(orphanRemoval = true, mappedBy = "restaurant")
    private List<MenuItem> menu;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private Image profileImage;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(orphanRemoval = true, mappedBy = "restaurant")
    private List<Like> likes;

    @OneToMany(orphanRemoval = true, mappedBy = "restaurant")
    private List<Rating> ratings;

    // private List<Reservation> reservations; ?

    Restaurant() {
        // Just for hibernate
    }

    public Restaurant(String name, String address, String phoneNumber, List<Tags> tags, User owner) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.tags = tags;
        this.owner = owner;
        // this.likes = 0;
        this.rating = 0;
        this.menu = new ArrayList<>();
    }

    public Restaurant(Long id, String name, String address, String phoneNumber, float rating, User owner){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.owner = owner;
        // this.likes = 0;
        this.menu = new ArrayList<>();
    }

    public Restaurant(Long id, String name, String address, String phoneNumber, float rating, User owner, int likes){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.owner = owner;
        // this.likes = likes;
        this.menu = new ArrayList<>();
    }

    public Restaurant(Long id, String name, String address, String phoneNumber, User owner){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.owner = owner;
        // this.likes=0;
        this.menu = new ArrayList<>();
    }

    public Long getId(){ return this.id; }
    public String getName(){ return this.name; }
    public String getAddress(){ return this.address; }
    public String getPhoneNumber(){ return this.phoneNumber; }
    public List<Rating> getRatings(){ return this.ratings; }
    public double getRating() { 
        return ratings
            .stream()
            .mapToDouble(r -> r.getRating()).average().orElse(0);
    }
    public User getOwner() { return owner; }
    public int getLikes() { return likes.size(); }
    // public int getLikes() { return likes; }
    public List<MenuItem> getMenu() { return this.menu; }
    public List<Tags> getTags() {return this.tags;}
    public Image getProfileImage() { return this.profileImage; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    // public void setRating(float rating) { this.rating = rating; }
    public void setOwner(User owner) { this.owner = owner; }
    // public void setLikes(int likes) { this.likes = likes; }
    public void setTags(List<Tags> tags) { this.tags = tags; }
    public void addMenuItem(MenuItem menuItem) { this.menu.add(menuItem); }
    public void setProfileImage(Image profileImage) { this.profileImage = profileImage; }
}
