package searchapp.service.impl;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchapp.config.UserAgent;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;


@Service
@Setter
@NoArgsConstructor
public class SiteMapImpl extends RecursiveAction implements searchapp.service.SiteMap {
    private final Set<SiteMapImpl> childs = new HashSet<>();

    private String url;

    private static String startUrl;

    private static final CopyOnWriteArraySet<String> allLinks = new CopyOnWriteArraySet<>();

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
                    .userAgent(UserAgent.getUaname())
                    .referrer(UserAgent.getUareferrer())
                    .get();
            Elements elements = doc.select("a[href]");
            elements.forEach(element -> {
                String newLink = element.absUrl("href");
                if (checkLink(newLink)) {
                    SiteMapImpl siteMapImpl = new SiteMapImpl(newLink, startUrl);
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

