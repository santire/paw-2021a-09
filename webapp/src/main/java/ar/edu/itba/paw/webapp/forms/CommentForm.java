package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Size;

public class CommentForm {

    private long id;

    @Size(min = 1, max = 144)
    private String review;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}
