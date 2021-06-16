package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

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
import javax.persistence.Transient;

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


    @Column(length = 100)
    private String facebook;

    @Column(length = 100)
    private String instagram;

    @Column(length = 100)
    private String twitter;


    // private List<Reservation> reservations; ?

    @Transient
    private List<MenuItem> menuPage;


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
        DecimalFormat df = new DecimalFormat("#.##");    
        double result = new Double(df.format(
        ratings
        .stream()
        .mapToDouble(r -> r.getRating()).average().orElse(0)));
        
        return result;
    }
    public User getOwner() { return owner; }
    public int getLikes() { return likes.size(); }
    // public int getLikes() { return likes; }
    public List<MenuItem> getMenu() { return this.menuPage; }
    public List<Tags> getTags() {return this.tags;}
    public Image getProfileImage() { return this.profileImage; }

    public String getFacebook() { return facebook; }
    public String getInstagram() { return instagram; }
    public String getTwitter() { return twitter; }



    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    // public void setRating(float rating) { this.rating = rating; }
    public void setOwner(User owner) { this.owner = owner; }
    // public void setLikes(int likes) { this.likes = likes; }
    public void setTags(List<Tags> tags) { this.tags = tags; }
    public void setMenu(List<MenuItem> menu) { this.menu = menu; }
    public void addMenuItem(MenuItem menuItem) { this.menu.add(menuItem); }
    public void setProfileImage(Image profileImage) { this.profileImage = profileImage; }


    public void setFacebook(String facebook) { this.facebook = facebook; }
    public void setInstagram(String instagram) { this.instagram = instagram; }
    public void setTwitter(String twitter) { this.twitter = twitter; }


    public List<MenuItem> getMenuPage() {
        return menuPage;
    }

    public void setMenuPage(List<MenuItem> menuPage) {
        this.menuPage = menuPage;
    }

}
