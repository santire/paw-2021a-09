package ar.edu.itba.paw.model;

import java.util.HashMap;
import java.util.Map;

public enum Tags {
    ARABE(0),
    AMERICANO(1),
    ARGENTINO(2),
    ARMENIO(3),
    ASIATICO(4),
    AUTOCTONO(5),
    BODEGON(6),
    CHINO(7),
    COCINACASERA(8),
    CONTEMPORANEA(9),
    DEAUTOR(10),
    DEFUSION(11),
    ESPAÑOL(12),
    FRANCES(13),
    INDIO(14),
    INTERNACIONAL(15),
    ITALIANO(16),
    JAPONES(17),
    LATINO(18),
    MEDITERRANEO(19),
    MEXICANO(20),
    PARRILLA(21),
    PERUANO(22),
    PESCADOSYMARISCOS(23),
    PICADAS(24),
    PIZZERIA(25),
    VEGETARIANO(26);

    private int value;
    private static Map<Integer, Tags> map = new HashMap<>();

    private Tags(int value) {
        this.value = value;
    }

    static {
        for (Tags tag : Tags.values()) {
            map.put(tag.value, tag);
        }
    }

    public static Tags valueOf(int tag) {
        return  map.get(tag);
    }

    public int getValue() {
        return value;
    }

    public static Map<Integer, Tags> allTags(){
        return  map;
    }

}
