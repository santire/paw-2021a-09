package ar.edu.itba.paw.webapp.dto;

public class RatingDto {
    private double rating;

    public RatingDto(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
