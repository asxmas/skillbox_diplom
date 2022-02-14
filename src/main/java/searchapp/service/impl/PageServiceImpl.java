package searchapp.service.impl;

import searchapp.config.Sites;
import searchapp.entity.Site;
import searchapp.entity.enums.SiteStatus;
import searchapp.mapper.IndexWrapper;
import searchapp.service.PageService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchapp.entity.Page;
import searchapp.repository.*;
import searchapp.service.IndexService;
import searchapp.service.LemmatizatorService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ForkJoinPool;


@Getter
@RequiredArgsConstructor
@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final FieldRepository fieldRepository;
    private final IndexRepository indexRepository;
    private final LemmaRepository lemmaRepository;
    private final SiteRepository siteRepository;
    private final LemmatizatorService lemmatizatorService;
    private final IndexService indexService;
    private final Sites sites;

    private String startUrl;
    private boolean isIndexing = false;
    private ForkJoinPool forkJoinPool;


    @Override
    public IndexWrapper getSiteMap(String startUrl){
        try {
            new URL(startUrl);
        }
        catch (MalformedURLException ex){
            return IndexWrapper.builder()
                    .result(false)
                    .error("Incorrect URL")
                    .build();
        }

        if(!urlIsAllowed(startUrl)){
            return IndexWrapper.builder()
                    .result(false)
                    .error("This URL not allowed in App")
                    .build();
        }

        siteRepository.findByUrl(startUrl).ifPresentOrElse((site) -> {
            site.setStatusTime(LocalDateTime.now());
            site.setStatus(SiteStatus.INDEXING);
        },() -> {
            Site site = new Site();
            site.setUrl(startUrl);
            site.setStatus(SiteStatus.INDEXING);
            site.setName(startUrl);
            site.setStatusTime(LocalDateTime.now());

        });
        SiteMapImpl site = new SiteMapImpl(startUrl, startUrl);
        forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(site);
        createPages(site.getLinks());

        return IndexWrapper.builder()
                .result(true).build();
    }

    @Override
    public IndexWrapper getSiteMaps(){
        if(isIndexing){
            return IndexWrapper.builder().result(true).build();
        }
        isIndexing = true;
        siteRepository.findAll().forEach(site -> getSiteMap(site.getUrl()));
        return IndexWrapper.builder()
                .result(false)
                .error("Indexing already launched").build();
    }

    @Override
    public Page createPage(String url){
        Page page = new Page();
        page.setPath(url);
        page.setCode(getCode(url));
        page.setContent(getContent(url));
        pageRepository.save(page);
        lemmatizatorService.generateLemms(page);
        indexService.createRank(page);
        return page;
    }

    public void createPages(String[] allLinks){
        HikariConfig config = new HikariConfig("/hikaricp.properties");
        HikariDataSource ds = new HikariDataSource(config);
        int processors = Runtime.getRuntime().availableProcessors();
        int blocks = allLinks.length/processors;
        int mod = allLinks.length%processors;
        String [][] links = new String[processors][];
        int srcPos = 0;
        Thread[] threads = new Thread[processors];
        for(int i = 0; i < processors; i++){
            int a = mod > 0 ? 1 : 0;
            links[i] = new String[blocks * (i + 1) + a - blocks * i];
            System.arraycopy(allLinks, srcPos, links[i], 0, links[i].length);
            int finalI = i;
            threads[i] = new Thread(
                    () -> {
                        try {
                            Thread.sleep((long)(Math.random()*(500 - 100) + 100));
                            ds.getConnection();
                        } catch (InterruptedException | SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        Arrays.stream(links[finalI]).forEach(this::createPage);
                    }
            );
            srcPos += links[i].length;
            mod--;
        }
       Arrays.stream(threads).forEach(Thread::start);
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public int getCode(String link) {
        int code = 404;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    @Override
    public String getContent(String link){

        StringBuilder stringBuffer = new StringBuilder();
        try {
            URL url = new URL(link);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            BufferedReader in = new BufferedReader(reader);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            reader.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @Override
    public IndexWrapper stopIndexing(){
        if(isIndexing){
            forkJoinPool.shutdown();
            isIndexing = false;
            return IndexWrapper.builder()
                    .result(true)
                    .build();
        }
        return IndexWrapper.builder()
                .result(false)
                .error("Индексация не запущена")
                .build();
    }

    private boolean urlIsAllowed(String url){
        for(Site site : sites.getSitesList()){
            if(site.getUrl().equals(url))
                return true;
            }
        return false;
    }
}
