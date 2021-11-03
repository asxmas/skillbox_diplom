package searchapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.stereotype.Service;
import searchapp.repository.dao.IndexDAO;
import searchapp.repository.dao.LemmaDAO;
import searchapp.repository.dao.impl.LemmaDAOImpl;
import searchapp.entity.Lemma;
import searchapp.entity.Page;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LemmatizatorImpl implements searchapp.service.Lemmatizator {
    private static LuceneMorphology luceneMorph = null;
    private final LemmaDAO lemmaDAO;

    static {
        try {
            luceneMorph = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Integer> getLemms(String text) {
        return Arrays.stream(text.toLowerCase(Locale.ROOT)
                //todo: уточнить по поводу английский слов, фирм-производителей и т.д.
                .replaceAll("[^а-яё\\-\s]", " ")
                .trim()
                .replaceAll("[\\s]{2,}", " ")
                .split(" "))
                .filter(word -> !isWord(word))
                .collect(Collectors.toMap(
                        word -> luceneMorph.getNormalForms(word.toLowerCase(Locale.ROOT)).get(0),
                        count -> 1, Integer::sum));
    }

    public void saveLemms(Map<String, Integer> lemms, Page page){
        lemms.forEach((word, count) -> {
            lemmaDAO.findLemmaByLemmaName(word).ifPresentOrElse(lemma
                            -> {
                lemma.setFrequency(lemma.getFrequency() + 1);
                    }
                    , () -> lemmaDAO.saveLemma(new Lemma(word)));
        });
    }
    
    private boolean isWord(String word){
        try {
            String[] lemArr = luceneMorph.getMorphInfo(word.toLowerCase(Locale.ROOT)).toArray(new String[0]);
            String clearWord = lemArr[lemArr.length - 1];
            return clearWord.contains("СОЮЗ") || clearWord.contains("МЕЖД") || clearWord.contains("ПРЕДЛ") || clearWord.contains("МС");

        } catch (Exception e){
            System.out.println(word);
            e.printStackTrace();
            return false;
        }

    }
}
