package searchapp.service;

import searchapp.service.impl.SiteMapImpl;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Future;

public interface SiteMap extends Serializable {
    void compute();

    void getChilds(Set<SiteMapImpl> subTask);

    String getUrl();

    Set<SiteMapImpl> getChildrenForMain();

    String[] getLinks();

    boolean checkLink(String link);
}
