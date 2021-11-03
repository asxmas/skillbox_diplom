package searchapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "field")
public class Field {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "selector", nullable = false)
    private String selector;

    @Column(name = "weight", nullable = false)
    private float weight;


    public Field(String nameAndSelector, float weight) {
        this.name = nameAndSelector;
        this.selector = nameAndSelector;
        this.weight = weight;
    }
}

