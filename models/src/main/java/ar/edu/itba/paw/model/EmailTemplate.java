package ar.edu.itba.paw.model;

public enum EmailTemplate {

    BUTTON("button_template"),
    BASIC("basic_template");

    private final String name;

    EmailTemplate(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
