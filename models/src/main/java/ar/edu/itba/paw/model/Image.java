package ar.edu.itba.paw.model;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Image {


    private final long imageId;
    private final byte[] data;


    public Image(byte[] data) {
        this.imageId = 0;
        this.data = data;
    }

    public Image(final long imageId, byte[] data){
        this.imageId = imageId;
        this.data = data;
    }

    public long getImageId(){ return this.imageId; }
    public byte[] getData(){ return this.data; }
    public String getImageEnconded() throws IOException{
        byte[] encodeBase64 = Base64.getEncoder().encode(this.data);
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
        return base64Encoded;
    }
}
