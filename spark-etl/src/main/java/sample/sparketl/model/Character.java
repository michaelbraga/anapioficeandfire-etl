package sample.sparketl.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Character implements Serializable {
    private final static long serialVersionUID = 301L;
    private String id;
    private String name;
    private String gender;
    private String culture;
    private String born;
    private String titles;
    private String aliases;
    private String mother;
    private String father;
    private String spouse;
    private String tvseries;
    private String playedby;
}