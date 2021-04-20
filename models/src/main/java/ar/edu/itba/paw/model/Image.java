package ar.edu.itba.paw.model;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Image {


    private static Image instance;
    private long imageId;
    private byte[] data;
    // private long ownerId;   // UserID or RestaurantID


    public Image(byte[] data) {
        this.imageId = 0;
        this.data = data;
    }

    public Image(final long imageId, byte[] data){
        this.imageId = imageId;
        this.data = data;
    }

    // public Image(final long imageId, byte[] data, long ownerId){
        // this.imageId = imageId;
        // this.data = data;
        // this.ownerId = ownerId;
    // }

    public Image(final long imageId, InputStream file) throws IOException {
        this.imageId = imageId;

        this.data = new byte[file.available()];
        //this.data= IOUtils.toByteArray(fi);
        file.read(data);
    }

    // public Image(byte[] data, long ownerId){
        // this.data = data;
        // this.ownerId = ownerId;
    // }

    public long getImageId(){ return this.imageId; }
    public byte[] getData(){ return this.data; }
    // public long getOwnerId() { return this.ownerId; }

    public String getImageEnconded() throws IOException{
        byte[] encodeBase64 = Base64.getEncoder().encode(this.data);
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
        return base64Encoded;
    }
}
