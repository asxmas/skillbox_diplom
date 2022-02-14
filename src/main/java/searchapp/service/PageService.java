package searchapp.service;

import searchapp.entity.Page;
import searchapp.mapper.IndexWrapper;

public interface PageService {

    IndexWrapper getSiteMap(String startUrl);

    IndexWrapper getSiteMaps();

    Page createPage(String url);

    int getCode(String link);

    String getContent(String link);

    IndexWrapper stopIndexing();
}
