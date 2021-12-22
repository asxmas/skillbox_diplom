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
import searchapp.service.LemmatizatorService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class SearchServiceImpl {

    private final PageDAO pageDAO;
    private final FieldDAO fieldDAO;
    private final IndexDAO indexDAO;
    private final LemmaDAO lemmaDAO;

    private final String searchQuery;

    public Map<Page, Float> getResultList(){
        LemmatizatorService lemmatizator = new LemmatizatorServiceImpl(lemmaDAO, fieldDAO);
        ArrayList<Integer> lemmaList = new ArrayList<>(lemmatizator.getLemmsForSearch(searchQuery)
                .stream()
                .map(Lemma::getId).toList());
        int firstLemmaId = lemmaList.get(0);
        List<Integer> pageIds = searchPages(lemmaList, indexDAO.findIndexesByLemmaId(firstLemmaId)
                                                            .stream()
                                                            .map(index -> index.getPage().getId())
                                                            .toList());
        Map<Integer, Float> mapWithRelevance = getMapWithRelevance(pageIds, lemmaList);
        Map<Page, Float> mapPageRank = new HashMap<>();
        mapWithRelevance.forEach((key, value) -> mapPageRank.put(pageDAO.findPageById(key), value));
        return mapPageRank;

    }

    private List<Integer> searchPages(List<Integer> lemmaList, List<Integer> pageList){
        ArrayList<Integer> lemmIdList = new ArrayList<>(lemmaList);
        List <Index> indexesFromPageIds = indexDAO.findIndexesByPageIds(pageList);
        List <Integer> pageIdList = indexesFromPageIds.stream()
                .filter(index -> index.getLemma().getId() == lemmaList.get(0))
                .sorted(Comparator.comparing(Index::getRank).reversed())
                .map(index -> index.getPage().getId())
                .toList();
        lemmIdList.remove(0);
        return lemmIdList.isEmpty() ? pageIdList
                                    : searchPages(lemmIdList, pageIdList);
    }

    private Map<Integer, Float> getMapWithRelevance (List<Integer> pageIdList, List<Integer> lemmIdList){
        List<Index> indexList = indexDAO.getIndexListForRelevance(pageIdList, lemmIdList);
        Map<Integer, Float> pageMapWithRelevance = new HashMap<>();
        for (Index index : indexList) {
            if(pageMapWithRelevance.containsKey(index.getPage().getId())){
                float value = pageMapWithRelevance.get(index.getPage().getId()) + index.getRank();
                pageMapWithRelevance.put(index.getPage().getId(), value);
            }
            else{
                pageMapWithRelevance.put(index.getPage().getId(), index.getRank());
            }
        }
        return pageMapWithRelevance;
    }
}
