package searchapp.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
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

@Service
@Getter
@RequiredArgsConstructor
public class SearchServiceImpl {

    private final PageDAO pageDAO;
    private final FieldDAO fieldDAO;
    private final IndexDAO indexDAO;
    private final LemmaDAO lemmaDAO;

    private final String searchQuery;

    public String getResultList() {
        LemmatizatorService lemmatizator = new LemmatizatorServiceImpl(lemmaDAO, fieldDAO);
        List<Lemma> lemmaList = new ArrayList<>(lemmatizator.getLemmsForSearch(searchQuery));
        ArrayList<Integer> lemmaIdsList = new ArrayList<>(lemmaList
                .stream()
                .map(Lemma::getId).toList());
        int firstLemmaId = lemmaIdsList.get(0);
        List<Integer> pageIds = searchPages(lemmaIdsList, indexDAO.findIndexesByLemmaId(firstLemmaId)
                .stream()
                .map(index -> index.getPage().getId())
                .toList());
        Map<Integer, Float> mapWithRelevance = getMapWithRelevance(pageIds, lemmaIdsList);
        Map<Page, Float> mapPageRank = new HashMap<>();
        mapWithRelevance.forEach((key, value) -> mapPageRank.put(pageDAO.findPageById(key), value));
        StringBuilder result = new StringBuilder();
        mapPageRank.entrySet().stream()
                .sorted(Map.Entry.<Page, Float>comparingByValue().reversed())
                .forEach(pageFloatEntry -> result.append("\nURL: ")
                        .append(pageFloatEntry.getKey().getPath())
                        .append("\nTITLE: ")
                        .append(Jsoup.parse(pageFloatEntry.getKey()
                                .getContent()).select("title").text())
                        .append("\nSNIPPET: ")
                        .append(getSentenceWithLemm(pageFloatEntry.getKey().getContent(), lemmaList))
                        .append("\nRELEVANCE ")
                        .append(pageFloatEntry.getValue())
                        .append("\n"));

        return result.toString();

    }

    private List<Integer> searchPages(List<Integer> lemmaList, List<Integer> pageList) {
        ArrayList<Integer> lemmIdList = new ArrayList<>(lemmaList);
        List<Index> indexesFromPageIds = indexDAO.findIndexesByPageIds(pageList);
        List<Integer> pageIdList = indexesFromPageIds.stream()
                .filter(index -> index.getLemma().getId() == lemmaList.get(0))
                .sorted(Comparator.comparing(Index::getRank).reversed())
                .map(index -> index.getPage().getId())
                .toList();
        lemmIdList.remove(0);
        return lemmIdList.isEmpty() ? pageIdList
                : searchPages(lemmIdList, pageIdList);
    }

    private Map<Integer, Float> getMapWithRelevance(List<Integer> pageIdList, List<Integer> lemmIdList) {
        List<Index> indexList = indexDAO.getIndexListForRelevance(pageIdList, lemmIdList);
        Map<Integer, Float> pageMapWithRelevance = new HashMap<>();
        for (Index index : indexList) {
            if (pageMapWithRelevance.containsKey(index.getPage().getId())) {
                float value = pageMapWithRelevance.get(index.getPage().getId()) + index.getRank();
                pageMapWithRelevance.put(index.getPage().getId(), value);
            } else {
                pageMapWithRelevance.put(index.getPage().getId(), index.getRank());
            }
        }
        return pageMapWithRelevance;
    }

    private String getSentenceWithLemm(String pageContent, List<Lemma> lemmaList) {
        LemmatizatorService lemmatizatorService = new LemmatizatorServiceImpl(lemmaDAO, fieldDAO);
        String[] parseText = Jsoup.parse(pageContent).text().replaceAll("[\\s]{2,}", " ").split(" ");
        List<String> sentenceWithLemms = new ArrayList<>();
        List<String> lemmaNamesList = lemmaList.stream().map(Lemma::getLemmaName).toList();
        for (String lemma : lemmaNamesList) {

            for (int i = 0; i < parseText.length; i++) {
                if (lemma.equals(lemmatizatorService.getLemm(parseText[i].toLowerCase(Locale.ROOT).replaceAll("[^a-zа-яё\\-\s]", "")))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    int j;
                    if (i > 5) {
                        if (i < parseText.length - 5) {
                            j = -5;
                        } else j = parseText.length - 10 - i;
                    } else j = -i;
                    for (int a = 0; a < 10; a++) {
                        if(lemmaNamesList.contains(lemmatizatorService.getLemm(parseText[i+j].toLowerCase(Locale.ROOT)))){
                            stringBuilder.append("<b>");
                            stringBuilder.append(parseText[i + j]).append("");
                            stringBuilder.append("</b> ");
                        }
                        stringBuilder.append(parseText[i + j]).append(" ");
                        j++;
                    }
                    sentenceWithLemms.add(stringBuilder.toString());
                    break;
                }
            }
        }
        StringBuilder result = new StringBuilder();
        sentenceWithLemms.forEach(sentence -> result.append(sentence).append("..."));
        return result.toString();
    }
}
