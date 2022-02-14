//package searchapp.service.impl;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.jsoup.Jsoup;
//import org.springframework.stereotype.Service;
//import searchapp.entity.Index;
//import searchapp.entity.Lemma;
//import searchapp.entity.Page;
//import searchapp.repository.FieldRepository;
//import searchapp.repository.IndexRepository;
//import searchapp.repository.LemmaRepository;
//import searchapp.repository.PageRepository;
//import searchapp.service.LemmatizatorService;
//
//import java.util.*;
//
//@Service
//@Getter
//@RequiredArgsConstructor
//public class SearchServiceImpl {
//
//    private final PageRepository pageRepository;
//    private final FieldRepository fieldRepository;
//    private final IndexRepository indexRepository;
//    private final LemmaRepository lemmaRepository;
//    private final LemmatizatorService lemmatizatorService;
//
//    private final String searchQuery;
//
//    public String getResultList() {
//        List<Lemma> lemmaList = new ArrayList<>(lemmatizatorService.getLemmsForSearch(searchQuery));
//        ArrayList<Integer> lemmaIdsList = new ArrayList<>(lemmaList
//                .stream()
//                .map(Lemma::getId).toList());
//        int firstLemmaId = lemmaIdsList.get(0);
//        List<Integer> pageIds = searchPages(lemmaIdsList, indexRepository.findAllByLemma(lemmaList.get(0))
//                .stream()
//                .map(Index::getPage)
//                .toList());
//        Map<Integer, Float> mapWithRelevance = getMapWithRelevance(pageIds, lemmaIdsList);
//        Map<Page, Float> mapPageRank = new HashMap<>();
//        mapWithRelevance.forEach((key, value) -> mapPageRank.put(pageRepository.findById(key).get(), value));
//        StringBuilder result = new StringBuilder();
//        mapPageRank.entrySet().stream()
//                .sorted(Map.Entry.<Page, Float>comparingByValue().reversed())
//                .forEach(pageFloatEntry -> result.append("\nURL: ")
//                        .append(pageFloatEntry.getKey().getPath())
//                        .append("\nTITLE: ")
//                        .append(Jsoup.parse(pageFloatEntry.getKey()
//                                .getContent()).select("title").text())
//                        .append("\nSNIPPET: ")
//                        .append(getSentenceWithLemm(pageFloatEntry.getKey().getContent(), lemmaList))
//                        .append("\nRELEVANCE ")
//                        .append(pageFloatEntry.getValue())
//                        .append("\n"));
//
//        return result.toString();
//
//    }
//
//    private List<Integer> searchPages(List<Integer> lemmaList, List<Page> pageList) {
//        ArrayList<Integer> lemmIdList = new ArrayList<>(lemmaList);
//        List<Index> indexesFromPageIds = indexRepository.findAllByPageIn(pageList);
//        List<Integer> pageIdList = indexesFromPageIds.stream()
//                .filter(index -> index.getLemma().getId() == lemmaList.get(0))
//                .sorted(Comparator.comparing(Index::getRank).reversed())
//                .map(index -> index.getPage().getId())
//                .toList();
//        lemmIdList.remove(0);
//        return lemmIdList.isEmpty() ? pageIdList
//                : searchPages(lemmIdList, pageList);
//    }
//
//    private Map<Integer, Float> getMapWithRelevance(List<Integer> pageIdList, List<Integer> lemmIdList) {
//        List<Index> indexList = indexRepository.getIndexListForRelevance(pageIdList, lemmIdList);
//        Map<Integer, Float> pageMapWithRelevance = new HashMap<>();
//        for (Index index : indexList) {
//            if (pageMapWithRelevance.containsKey(index.getPage().getId())) {
//                float value = pageMapWithRelevance.get(index.getPage().getId()) + index.getRank();
//                pageMapWithRelevance.put(index.getPage().getId(), value);
//            } else {
//                pageMapWithRelevance.put(index.getPage().getId(), index.getRank());
//            }
//        }
//        return pageMapWithRelevance;
//    }
//
//    private String getSentenceWithLemm(String pageContent, List<Lemma> lemmaList) {
//        String[] parseText = Jsoup.parse(pageContent).text().replaceAll("[\\s]{2,}", " ").split(" ");
//        List<String> sentenceWithLemms = new ArrayList<>();
//        List<String> lemmaNamesList = lemmaList.stream().map(Lemma::getLemmaName).toList();
//        for (String lemma : lemmaNamesList) {
//
//            for (int i = 0; i < parseText.length; i++) {
//                if (lemma.equals(lemmatizatorService.getLemm(parseText[i].toLowerCase(Locale.ROOT).replaceAll("[^a-zа-яё\\-\s]", "")))) {
//                    StringBuilder stringBuilder = new StringBuilder();
//                    int j;
//                    if (i > 5) {
//                        if (i < parseText.length - 5) {
//                            j = -5;
//                        } else j = parseText.length - 10 - i;
//                    } else j = -i;
//                    for (int a = 0; a < 10; a++) {
//                        if(lemmaNamesList.contains(lemmatizatorService.getLemm(parseText[i+j].toLowerCase(Locale.ROOT)))){
//                            stringBuilder.append("<b>");
//                            stringBuilder.append(parseText[i + j]).append("");
//                            stringBuilder.append("</b> ");
//                        }
//                        stringBuilder.append(parseText[i + j]).append(" ");
//                        j++;
//                    }
//                    sentenceWithLemms.add(stringBuilder.toString());
//                    break;
//                }
//            }
//        }
//        StringBuilder result = new StringBuilder();
//        sentenceWithLemms.forEach(sentence -> result.append(sentence).append("..."));
//        return result.toString();
//    }
//}
