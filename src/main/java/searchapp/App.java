package searchapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Autowired;
import searchapp.entity.Field;
import searchapp.entity.Page;
import searchapp.repository.dao.FieldDAO;
import searchapp.repository.dao.IndexDAO;
import searchapp.repository.dao.LemmaDAO;
import searchapp.repository.dao.PageDAO;
import searchapp.repository.dao.impl.FieldDAOImpl;
import searchapp.repository.dao.impl.IndexDAOImpl;
import searchapp.repository.dao.impl.LemmaDAOImpl;
import searchapp.repository.dao.impl.PageDAOimpl;
import searchapp.service.impl.PageServiceImpl;

public class App {
    private static final Logger log = LogManager.getRootLogger();
    private static final Marker INFO_MES = MarkerManager.getMarker("INFO_MES");




    public static void main(String[] args) {
        PageDAO pageDAO = new PageDAOimpl();
        FieldDAO fieldDAO = new FieldDAOImpl();
        IndexDAO indexDAO = new IndexDAOImpl();
        LemmaDAO lemmaDAO = new LemmaDAOImpl();
        log.info(INFO_MES, "Старт");

        fieldDAO.saveField(new Field("title", 1.0f));
        fieldDAO.saveField(new Field("body", 0.8f));
        String startUrl = "http://www.playback.ru/";
        PageServiceImpl pageService = new PageServiceImpl(pageDAO, fieldDAO, indexDAO, lemmaDAO, startUrl);
        pageService.getSiteMap();

        log.error("Окончание");

    }


}
