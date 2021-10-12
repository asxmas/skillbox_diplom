package searchapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import searchapp.service.impl.PageServiceImpl;

public class App {
    private static final Logger log = LogManager.getRootLogger();
    private static final Marker INFO_MES = MarkerManager.getMarker("INFO_MES");


    public static void main(String[] args) {
        log.info(INFO_MES, "Старт");
        String startUrl = "http://www.playback.ru/";
        PageServiceImpl pageService = new PageServiceImpl(startUrl);
        pageService.getSiteMap();

        log.error("Окончание");

    }


}
