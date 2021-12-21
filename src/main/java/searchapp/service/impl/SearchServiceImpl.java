package searchapp.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchapp.entity.Index;
import searchapp.entity.Lemma;
import searchapp.entity.Page;
import searchapp.repository.dao.FieldDAO;
import searchapp.repository.dao.IndexDAO;
import searchapp.repository.dao.LemmaDAO;
import searchapp.repository.dao.PageDAO;
import searchapp.service.Lemmatizator;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public class SearchServiceImpl {

    private final PageDAO pageDAO;
    private final FieldDAO fieldDAO;
    private final IndexDAO indexDAO;
    private final LemmaDAO lemmaDAO;

    private final String searchQuery;

    public List<Page> getResultList(){
        Lemmatizator lemmatizator = new LemmatizatorServiceImpl(lemmaDAO, fieldDAO);
        return searchPages(lemmatizator.getLemmsForSearch(searchQuery), indexDAO.findAllIndexes());

    }

    public List<Page> searchPages(List<Lemma> lemmaList, List<Index> indexList){
        ArrayList<Lemma> lemmList = new ArrayList<>(lemmaList);
        Lemma lemma = lemmList.get(0);
        List <Integer> pageIdList = indexList.stream()
                .filter(index -> index.getLemma().getId() == lemma.getId())
                .map(index -> index.getPage().getId()).toList();

        lemmList.remove(0);
        return lemmaList.isEmpty() ? pageDAO.findPagesByIds(pageIdList) : searchPages(lemmaList, indexDAO.findIndexesByPageIds(pageIdList));
    }
}
