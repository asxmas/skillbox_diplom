package searchapp.service.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;

@Service
public class SiteMap extends RecursiveAction {
    private final Set<SiteMap> childs = new HashSet<>();

    private final String url;

    private static String startUrl;

    private static CopyOnWriteArraySet<String> allLinks = new CopyOnWriteArraySet<>();

    public SiteMap(String url) {
        this.url = url;
    }

    public SiteMap(String url, String startUrl) {
        this.url = url;
        SiteMap.startUrl = startUrl;
    }

    @Override
    protected void compute() {
        Set<SiteMap> subTask = new HashSet<>();
        getChilds(subTask);
        for(SiteMap siteMap : subTask){
            siteMap.join();
        }
    }

    public void getChilds(Set<SiteMap> subTask) {
        try {
            Thread.sleep((long)(Math.random()*(500 - 100) + 100));
            Document doc = Jsoup.connect(this.url).maxBodySize(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) YaBrowser/21.9.0.1044")
                    .referrer("http://www.google.com")
                    .get();
            Elements elements = doc.select("a[href]");
            elements.forEach(element -> {
                String newLink = element.absUrl("href");
                if (checkLink(newLink)) {
                    SiteMap siteMap = new SiteMap(newLink);
                    siteMap.fork();
                    allLinks.add(newLink);
                    subTask.add(siteMap);
                }
            });
        } catch (InterruptedException | IOException e) {

            allLinks.add(e.getMessage().replaceAll(".*=URL", ""));
        }
    }

    public String getUrl() {
        return url;
    }

    public Set<SiteMap> getChildrenForMain (){
        return childs;
    }

    public String[] getLinks(){
        String [] array = new String[allLinks.size()];
        array = allLinks.toArray(array);
        return array;
    }

    private boolean checkLink(String link){
       return !link.isEmpty()
               && link.startsWith(startUrl)
               && !link.matches(".*\\.[a-zA-Z1-9]{3,4}$")
               && !allLinks.contains(link)
               && !link.contains("#");
    }


}

