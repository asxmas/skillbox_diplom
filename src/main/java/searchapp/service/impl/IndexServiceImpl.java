package searchapp.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import searchapp.entity.Index;
import searchapp.entity.Lemma;
import searchapp.entity.Page;
import searchapp.repository.dao.FieldDAO;
import searchapp.repository.dao.IndexDAO;
import searchapp.repository.dao.LemmaDAO;
import searchapp.repository.dao.impl.IndexDAOImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Service
public class IndexServiceImpl implements searchapp.service.IndexService {
    private final IndexDAO indexDAO;
    private final LemmaDAO lemmaDAO;
    private final FieldDAO fieldDAO;

    @Override
    public void createRank(Page page){
        LemmatizatorServiceImpl lem = new LemmatizatorServiceImpl(lemmaDAO, fieldDAO);
        HashMap<String, Float> words = new HashMap<>();
        Document doc = Jsoup.parse(page.getContent());
        fieldDAO.findAllFields().forEach(field -> {
            Elements elements = doc.select(field.getSelector());
            elements.forEach(element -> {
                String text = Jsoup.parse(element.toString()).text();
                lem.getLemms(text).forEach((word, count) -> words.merge(word, count * field.getWeight(), Float::sum));
            });
        });
        saveRank(page, words);

    }

    @Override
    public void saveRank(Page page, Map<String, Float> words) {

    }

}
