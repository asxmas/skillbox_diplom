package searchapp.service;

import searchapp.entity.Index;
import searchapp.entity.Lemma;
import searchapp.entity.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IndexService {
    void createRank(Page page);

    void saveRank(Page page, Map<String, Float> words);
}
