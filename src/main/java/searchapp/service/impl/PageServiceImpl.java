package searchapp.service.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchapp.entity.Page;
import searchapp.repository.dao.FieldDAO;
import searchapp.repository.dao.IndexDAO;
import searchapp.repository.dao.LemmaDAO;
import searchapp.repository.dao.PageDAO;
import searchapp.service.IndexService;
import searchapp.service.LemmatizatorService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

@Getter
@RequiredArgsConstructor
@Service
public class PageServiceImpl implements searchapp.service.PageService {

    private final PageDAO pageDAO;
    private final FieldDAO fieldDAO;
    private final IndexDAO indexDAO;
    private final LemmaDAO lemmaDAO;

    private final String startUrl;

    @Override
    public void getSiteMap(){

        SiteMapImpl site = new SiteMapImpl(startUrl, startUrl);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(site);
        createPages(site.getLinks());

    }

    @Override
    public Page createPage(String url){
        LemmatizatorService lemmatizator = new LemmatizatorServiceImpl(lemmaDAO, fieldDAO);
        IndexService indexService = new IndexServiceImpl(indexDAO, lemmaDAO, fieldDAO);
        Page page = new Page();
        page.setPath(url);
        page.setCode(getCode(url));
        page.setContent(getContent(url));
        pageDAO.savePage(page);
        lemmatizator.generateLemms(page);
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




}
