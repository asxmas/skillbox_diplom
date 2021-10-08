package searchapp.service;

public interface PageService {

    void getSiteMap();

    void createPage(String url);

    int getCode(String link);

    String getContent(String link);

}
