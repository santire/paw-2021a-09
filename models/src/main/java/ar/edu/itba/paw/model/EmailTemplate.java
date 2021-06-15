package ar.edu.itba.paw.model;

public enum EmailTemplate {

    REGISTER("register"),
    FORGOT_PASSWORD("forgot_password");

    private final String name;

    EmailTemplate(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
