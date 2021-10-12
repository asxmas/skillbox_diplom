package searchapp.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LemmatizatorImpl implements searchapp.service.Lemmatizator {
    private static LuceneMorphology luceneMorph = null;

    static {
        try {
            luceneMorph = new RussianLuceneMorphology();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String text;

    @Override
    public Map<String, Integer> getLemms() {
        return Arrays.stream(text.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-zа-яЁ\\-\s]", "")
                .replaceAll("[\\s]{2,}", " ")
                .split(" "))
                .filter(word -> !isWord(word))
                .collect(Collectors.toMap(
                        word -> luceneMorph.getNormalForms(word.toLowerCase(Locale.ROOT)).get(0),
                        count -> 1, Integer::sum));
    }
    
    private boolean isWord(String word){
        String [] lemArr = luceneMorph.getMorphInfo(word).toArray(new String[0]);
        String clearWord = lemArr[lemArr.length - 1];
        return clearWord.contains("СОЮЗ") || clearWord.contains("МЕЖД") || clearWord.contains("ПРЕДЛ") || clearWord.contains("МС");
    }
}
