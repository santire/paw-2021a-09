package ar.edu.itba.paw.webapp.dto;

public class LikeDto {
    private boolean isLiked;

    public LikeDto(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
