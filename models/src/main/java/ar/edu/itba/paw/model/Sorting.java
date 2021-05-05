package ar.edu.itba.paw.model;

import java.util.Arrays;
import java.util.List;

public enum Sorting {
    HOT("hot"),
    PRICE("price"),
    LIKES("likes"),
    NAME("name");

    private String sortType;

    Sorting(String sortType){
        this.sortType = sortType;
    }
    
    public String getSortType() {
        return sortType;
    }

    public static List<Sorting> getSortTypes() {
        return Arrays.asList(Sorting.values());
    }

}
