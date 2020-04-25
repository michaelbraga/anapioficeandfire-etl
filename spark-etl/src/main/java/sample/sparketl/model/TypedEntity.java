package sample.sparketl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypedEntity implements Serializable {
    private final static long serialVersionUID = 100L;
    private String type;
    private String data;
}