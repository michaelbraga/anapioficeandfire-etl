package sample.sparketl.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class House implements Serializable {
    private final static long serialVersionUID = 401L;
    private String id;
    private String name;
    private String region;
    private String coatofarms;
    private String words;
    private String titles;
    private String seats;
    private String founded;
    private String founder;
    private String diedout;
    private String ancestralweapons;
}