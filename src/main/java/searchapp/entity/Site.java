package searchapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import searchapp.entity.enums.SiteStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "site")
public class Site {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "status", nullable = false, unique = true)
    private SiteStatus status;

    @Column(name = "status_time", nullable = false)
    private LocalDateTime statusTime;

    @Column(name = "last_error")
    private LocalDateTime lastError;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "pages", fetch = FetchType.LAZY)
    private Set<Lemma> lemms;
}
