package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

@Entity
@Table(name = "Restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurants_restaurant_id_seq")
    @SequenceGenerator(sequenceName = "restaurants_restaurant_id_seq", name = "restaurants_restaurant_id_seq", allocationSize = 1)
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
    private int likes;

    // TODO: Figure out how to map this
    private List<Tags> tags;

    @OneToMany(orphanRemoval = true, mappedBy = "restaurant")
    private List<MenuItem> menu;

    @OneToOne(mappedBy = "restaurant")
    private Optional<Image> profileImage;

    Restaurant() {
        // Just for hibernate
    }
    public Restaurant(Long id, String name, String address, String phoneNumber, float rating, User owner){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.owner = owner;
        this.likes = 0;
        this.menu = new ArrayList<>();
    }

    public Restaurant(Long id, String name, String address, String phoneNumber, float rating, User owner, int likes){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.owner = owner;
        this.likes = likes;
        this.menu = new ArrayList<>();
    }

    public Restaurant(Long id, String name, String address, String phoneNumber, User owner){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.owner = owner;
        this.likes=0;
        this.menu = new ArrayList<>();
    }

    public Long getId(){ return this.id; }
    public String getName(){ return this.name; }
    public String getAddress(){ return this.address; }
    public String getPhoneNumber(){ return this.phoneNumber; }
    public float getRating() { return rating; }
    public User getOwner() { return owner; }
    public int getLikes() { return likes; }
    public List<MenuItem> getMenu() { return this.menu; }
    public List<Tags> getTags() {return this.tags;}
    public Optional<Image> getMaybeProfileImage() { return this.profileImage; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setRating(float rating) { this.rating = rating; }
    public void setOwner(User owner) { this.owner = owner; }
    public void setLikes(int likes) { this.likes = likes; }
    public void setTags(List<Tags> tags) { this.tags = tags; }
    public void addMenuItem(MenuItem menuItem) { this.menu.add(menuItem); }
    public void setProfileImage(Image profileImage) { this.profileImage = Optional.ofNullable(profileImage); }
}
