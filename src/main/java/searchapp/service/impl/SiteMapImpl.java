package searchapp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;

@Service
public class SiteMapImpl extends RecursiveAction implements searchapp.service.SiteMap {
    private final Set<SiteMapImpl> childs = new HashSet<>();

    private final String url;

    private static String startUrl;

    private static final CopyOnWriteArraySet<String> allLinks = new CopyOnWriteArraySet<>();

    public SiteMapImpl(String url) {
        this.url = url;
    }

    public SiteMapImpl(String url, String startUrl) {
        this.url = url;
        SiteMapImpl.startUrl = startUrl;
    }

    @Override
    public void compute() {
        Set<SiteMapImpl> subTask = new HashSet<>();
        getChilds(subTask);
        for(SiteMapImpl siteMapImpl : subTask){
            siteMapImpl.join();
        }
    }

    @Override
    public void getChilds(Set<SiteMapImpl> subTask) {
        try {
            Thread.sleep((long)(Math.random()*(500 - 100) + 100));
            Document doc = Jsoup.connect(this.url).maxBodySize(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 YaBrowser/21.9.0.1044 Yowser/2.5 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
            Elements elements = doc.select("a[href]");
            elements.forEach(element -> {
                String newLink = element.absUrl("href");
                if (checkLink(newLink)) {
                    SiteMapImpl siteMapImpl = new SiteMapImpl(newLink);
                    siteMapImpl.fork();
                    allLinks.add(newLink);
                    subTask.add(siteMapImpl);
                }
            });
        } catch (InterruptedException | IOException e) {
            allLinks.add(e.getMessage().replaceAll(".*=URL", ""));
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Set<SiteMapImpl> getChildrenForMain(){
        return childs;
    }

    @Override
    public String[] getLinks(){
        String [] array = new String[allLinks.size()];
        array = allLinks.toArray(array);
        return array;
    }

    @Override
    public boolean checkLink(String link){
       return !link.isEmpty()
               && link.startsWith(startUrl)
               && (link.endsWith("html") || !link.matches(startUrl + ".+\\.[a-zA-Z1-9]{3,4}$"))
               && !allLinks.contains(link)
               && !link.contains("#");
    }


}

