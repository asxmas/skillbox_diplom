package searchapp.service;

import searchapp.entity.Lemma;
import searchapp.entity.Page;

import java.util.List;
import java.util.Map;

public interface LemmatizatorService {
    Map<String, Integer> getLemms(String text);

    List<Lemma> getLemmsForSearch(String text);

    void generateLemms(Page page);

    void saveLemms(Map<String, Integer> lemms, Page page);
}
