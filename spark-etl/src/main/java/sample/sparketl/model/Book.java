package sample.sparketl.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Book implements Serializable {
    private final static long serialVersionUID = 201L;
    private String id;
    private String name;
    private String isbn;
    private String authors;
    private int numberofpages;
    private String publisher;
    private String country;
    private String mediatype;
    private Timestamp released;
}