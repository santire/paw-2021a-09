package ar.edu.itba.paw.model;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "restaurant_images")
public class Image {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_images_image_id_seq")
    @SequenceGenerator(sequenceName = "restaurant_images_image_id_seq", name = "restaurant_images_image_id_seq", allocationSize = 1)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name="image_data")
    private byte[] data;

    @OneToOne
    @MapsId
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;


    Image(){
        // Just for hibernate
    }

    public Image(byte[] data) {
        this.imageId = null;
        this.data = data;
    }

    public Image(final long imageId, byte[] data){
        this.imageId = imageId;
        this.data = data;
    }

    public Long getImageId(){ return this.imageId; }
    public byte[] getData(){ return this.data; }
    public String getImageEnconded() throws IOException{
        byte[] encodeBase64 = Base64.getEncoder().encode(this.data);
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
        return base64Encoded;
    }
    public Restaurant getRestaurant() { return restaurant; }

    public void setImageId(Long imageId) { this.imageId = imageId; }
    public void setData(byte[] data) { this.data = data; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

}
