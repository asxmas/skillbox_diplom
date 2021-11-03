package searchapp.service;

import searchapp.entity.Page;

import java.util.Map;

public interface Lemmatizator {
    Map<String, Integer> getLemms(String text);

    void saveLemms(Map<String, Integer> lemms, Page page);
}
