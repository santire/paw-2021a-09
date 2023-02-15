package ar.edu.itba.paw.model;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;


@Entity
@Table(name = "restaurant_images")
public class Image {
    private static final String placeHolder = "data:image/jpeg;base64,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AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/2Q==";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_images_image_id_seq")
    @SequenceGenerator(sequenceName = "restaurant_images_image_id_seq", name = "restaurant_images_image_id_seq", allocationSize = 1)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name="image_data")
    private byte[] data;

    @OneToOne
//    @PrimaryKeyJoinColumn(name = "restaurant_id")
    @JoinColumn(name ="restaurant_id", nullable = false)
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

    public Image(final String base64){
        this.data = Base64.getEncoder().encode(base64.getBytes(StandardCharsets.UTF_8));
    }

    public Long getImageId(){ return this.imageId; }
    public byte[] getData(){ return this.data; }

    public static byte[] getPlaceholderImage() {
        byte[] data = Base64.getEncoder().encode(placeHolder.getBytes(StandardCharsets.UTF_8));
        String decoded = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
        String[] splitImage = decoded.split(",");
        return DatatypeConverter.parseBase64Binary(splitImage[1]);
    }


    public Restaurant getRestaurant() { return restaurant; }

    public void setImageId(Long imageId) { this.imageId = imageId; }
    public void setData(byte[] data) { this.data = data; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }



}
