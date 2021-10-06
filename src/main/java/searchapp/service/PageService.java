package searchapp.service;

import searchapp.service.impl.SiteMap;
import java.util.TreeSet;

public interface PageService {
    void getNodes(SiteMap siteMap, TreeSet<String> links);

    void getSiteMap();

    void createPage(String url);

    int getCode(String link);

    String getContent(String link);

    searchapp.DAO.PageDAO getPageDAO();

    String getStartUrl();
}
