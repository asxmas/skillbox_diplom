package searchapp.service;

import searchapp.entity.Page;

public interface PageService {

//    void getSiteMap();

    Page createPage(String url);

    int getCode(String link);

    String getContent(String link);

}
