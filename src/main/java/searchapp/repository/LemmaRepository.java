package searchapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import searchapp.entity.Lemma;

import java.util.List;
import java.util.Optional;

@Repository
public interface LemmaRepository extends JpaRepository <Lemma, Integer> {

    Optional<Lemma> findLemmaByLemmaName (String lemmaName);

    List<Lemma> findAllByLemmaNameIn(List<String> lemmaName);

    @Transactional
    @Modifying
    @Query("update Lemma l set l.frequency = ?1 where l.id = ?2")
    void updateLemma(Integer frequency, Integer lemmaId);


}
