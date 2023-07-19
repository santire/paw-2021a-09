package ar.edu.itba.paw.webapp.dto;


import ar.edu.itba.paw.model.Image;


public class ImageDto {


    private byte[] data;


    public static ImageDto fromImage(Image image){
        final ImageDto dto = new ImageDto();
        dto.data = image.getData();

        return  dto;
    }
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
