package searchapp.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import searchapp.entity.Page;
import searchapp.repository.FieldRepository;
import searchapp.repository.IndexRepository;
import searchapp.repository.LemmaRepository;
import searchapp.service.LemmatizatorService;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Service
public class IndexServiceImpl implements searchapp.service.IndexService {
    private final IndexRepository indexRepository;
    private final LemmaRepository lemmaRepository;
    private final FieldRepository fieldRepository;
    private final LemmatizatorService lemmatizatorService;

    @Override
    public void createRank(Page page){
        HashMap<String, Float> words = new HashMap<>();
        Document doc = Jsoup.parse(page.getContent());
        fieldRepository.findAll().forEach(field -> {
            Elements elements = doc.select(field.getSelector());
            elements.forEach(element -> {
                String text = Jsoup.parse(element.toString()).text();
                lemmatizatorService.getLemms(text).forEach((word, count) -> words.merge(word, count * field.getWeight(), Float::sum));
            });
        });
        saveRank(page, words);

    }

    @Override
    public void saveRank(Page page, Map<String, Float> words) {

    }

}
