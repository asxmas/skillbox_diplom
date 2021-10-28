package searchapp.dao;

import searchapp.entity.Lemma;

import java.util.List;
import java.util.Optional;

public interface LemmaDAO {
    Lemma findLemmaById(int id);

    Optional<Lemma> findLemmaByLemmaName(String lemmaName);

    void saveLemma(Lemma lemma);

    void updateLemma(Lemma lemma);

    void deleteLemma(Lemma lemma);

    List<Lemma> findAllLemmas();
}
