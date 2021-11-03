package searchapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "lemma")
public class Lemma {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "lemma", nullable = false, unique = true)
    private String lemmaName;

    @Column(name = "frequency", nullable = false)
    private int frequency;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "index",
            joinColumns = @JoinColumn(name = "lemma_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "page_id", nullable = false))
    private Set<Page> pages;

    public Lemma(String name){
        this.lemmaName = name;
        this.frequency = 1;
    }

}
