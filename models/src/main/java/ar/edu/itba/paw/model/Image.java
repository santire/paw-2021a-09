package ar.edu.itba.paw.model;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Image {

    private long imageId;
    private byte[] data;
    private String type;    // Extension (e.g jpg)
    private long ownerId;   // UserID or RestaurantID


    public Image(final long imageId, byte[] data, String type){
        this.imageId = imageId;
        this.data = data;
        this.type = type;
    }

    public Image(final long imageId, byte[] data, String type, long ownerId){
        this.imageId = imageId;
        this.data = data;
        this.type = type;
        this.ownerId = ownerId;
    }

    public Image(final long imageId, InputStream file, String type) throws IOException {
        this.imageId = imageId;
        this.type = type;

        this.data = new byte[file.available()];
        //this.data= IOUtils.toByteArray(fi);
        file.read(data);
    }

    public Image(byte[] data, String type, long ownerId){
        this.data = data;
        this.type = type;
        this.ownerId = ownerId;
    }

    public long getImageId(){ return this.imageId; }
    public byte[] getData(){ return this.data; }
    public String getType(){ return this.type; }
    public long getOwnerId() { return this.ownerId; }

    // The other way is made in Controller
    public String byteArrayToImage() throws IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(this.data);
        BufferedImage bImage = ImageIO.read(bis);
        File file = new File("profile.jpg");
        ImageIO.write(bImage, "jpg", file);

        byte[] encodeBase64 = Base64.getEncoder().encode(this.data);
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
        return base64Encoded;
    }
}
