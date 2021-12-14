package searchapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.stereotype.Service;
import searchapp.repository.dao.LemmaDAO;
import searchapp.entity.Lemma;
import searchapp.entity.Page;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LemmatizatorImpl implements searchapp.service.Lemmatizator {
    private static LuceneMorphology luceneRusMorph = null;
    private static LuceneMorphology luceneEngMorph = null;
    private final LemmaDAO lemmaDAO;

    static {
        try {
            luceneRusMorph = new RussianLuceneMorphology();
            luceneEngMorph = new EnglishLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Integer> getLemms(String text) {
        Map<String, Integer> engLemms = Arrays.stream(text.toLowerCase(Locale.ROOT)
                        .replaceAll("[^a-z\\-\s]", "")
                        .replaceAll("[\\s]{2,}", " ")
                        .split(" "))
                .filter(this::isEngWord)
                .collect(Collectors.toMap(
                        word -> luceneEngMorph.getNormalForms(word.toLowerCase(Locale.ROOT)).get(0),
                        count -> 1, Integer::sum));
        Map<String, Integer> rusLemms = Arrays.stream(text.toLowerCase(Locale.ROOT)
                        .replaceAll("[^а-яё\\-\s]", "")
                        .replaceAll("[\\s]{2,}", " ")
                        .split(" "))
                .filter(this::isRusWord)
                .collect(Collectors.toMap(
                        word -> luceneRusMorph.getNormalForms(word.toLowerCase(Locale.ROOT)).get(0),
                        count -> 1, Integer::sum));
        Map<String, Integer> allLemms = new HashMap<>(engLemms);
        allLemms.putAll(rusLemms);

        return allLemms;
    }

    public void saveLemms(Map<String, Integer> lemms, Page page){

        lemms.forEach((word, count) -> lemmaDAO.findLemmaByLemmaName(word).ifPresentOrElse(lemma
                        -> {
            lemma.setFrequency(lemma.getFrequency() + 1);
            lemmaDAO.updateLemma(lemma);
                }
                , () -> {
                    Lemma lemma = new Lemma();
                    lemma.setLemmaName(word);
                    lemma.setFrequency(1);
                    lemmaDAO.saveLemma(lemma);

                }));
    }

    public boolean isRusWord(String word){
        if(word.matches("[а-яё]+")) {
            String[] lemArr = luceneRusMorph.getMorphInfo(word).toArray(new String[0]);
            String clearWord = lemArr[lemArr.length - 1];
            return !(clearWord.matches(".*(СОЮЗ|МЕЖД|ПРЕДЛ|МС).*"));
        }
        return false;
    }

    public boolean isEngWord(String word){
        if(word.matches("[a-z]+")) {
            String[] lemArr = luceneEngMorph.getMorphInfo(word).toArray(new String[0]);
            String clearWord = lemArr[lemArr.length - 1];
            return !clearWord.matches(".*(ARTICLE|ADJECTIVE|CONJ|PREP|INT|VBE|PART|ADVERB).*");
        }
        return false;
    }
}
