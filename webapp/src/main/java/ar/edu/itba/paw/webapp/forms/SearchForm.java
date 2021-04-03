package ar.edu.itba.paw.webapp.forms;

import javax.validation.constraints.Size;

public class SearchForm {
    @Size(min = 1, max = 100)
    private String search;

    public void setSearch(String search){ this.search = search; }
    public String getSearch(){ return this.search; }
}
