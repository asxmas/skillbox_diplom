package searchapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import searchapp.entity.Lemma;
import searchapp.entity.Page;
import searchapp.repository.dao.FieldDAO;
import searchapp.repository.dao.IndexDAO;
import searchapp.repository.dao.LemmaDAO;
import searchapp.repository.dao.PageDAO;
import searchapp.repository.dao.impl.FieldDAOImpl;
import searchapp.repository.dao.impl.IndexDAOImpl;
import searchapp.repository.dao.impl.LemmaDAOImpl;
import searchapp.repository.dao.impl.PageDAOimpl;
import searchapp.service.LemmatizatorService;
import searchapp.service.impl.LemmatizatorServiceImpl;
import searchapp.service.impl.SearchServiceImpl;

import java.util.List;
import java.util.Map;

public class App {
    private static final Logger log = LogManager.getRootLogger();
    private static final Marker INFO_MES = MarkerManager.getMarker("INFO_MES");

    public static void main(String[] args) {

        PageDAO pageDAO = new PageDAOimpl();
        FieldDAO fieldDAO = new FieldDAOImpl();
        IndexDAO indexDAO = new IndexDAOImpl();
        LemmaDAO lemmaDAO = new LemmaDAOImpl();
        log.info(INFO_MES, "Старт");

//        fieldDAO.saveField(new Field("title", 1.0f));
//        fieldDAO.saveField(new Field("body", 0.8f));
//        String startUrl = "http://www.playback.ru/";
//        PageServiceImpl pageService = new PageServiceImpl(pageDAO, fieldDAO, indexDAO, lemmaDAO, startUrl);
//        pageService.getSiteMap();
//        pageService.createPage(startUrl);
//        pageService.createPage("http://www.playback.ru/contacts.html");
//        Lemma lemma = lemmaDAO.findLemmaByLemmaName("контакт").get();
//        System.out.println(lemma.getFrequency());
//        lemma.setFrequency(lemma.getFrequency()+1);
//        System.out.println(lemma.getFrequency());
        String searchQuery = "магазин xiaomi купить разблокировка";
//        LemmatizatorService lemmatizator = new LemmatizatorServiceImpl(lemmaDAO, fieldDAO);
//        List<Lemma> lemmaList = lemmatizator.getLemmsForSearch(searchQuery);
//        lemmaList.forEach(lemma -> System.out.println(lemma.getLemmaName()));

        SearchServiceImpl searchService = new SearchServiceImpl(pageDAO, fieldDAO, indexDAO, lemmaDAO, searchQuery);
        System.out.println(searchService.getResultList());
        log.error("Окончание");

    }


}
