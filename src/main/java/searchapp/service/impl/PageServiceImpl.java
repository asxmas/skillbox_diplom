package searchapp.service.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchapp.entity.Index;
import searchapp.entity.Page;
import searchapp.repository.dao.FieldDAO;
import searchapp.repository.dao.IndexDAO;
import searchapp.repository.dao.LemmaDAO;
import searchapp.repository.dao.PageDAO;
import searchapp.repository.dao.impl.FieldDAOImpl;
import searchapp.repository.dao.impl.IndexDAOImpl;
import searchapp.repository.dao.impl.LemmaDAOImpl;
import searchapp.repository.dao.impl.PageDAOimpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

@Getter
@RequiredArgsConstructor
@Service
public class PageServiceImpl implements searchapp.service.PageService {

    private final PageDAO pageDAO;
    private final FieldDAO fieldDAO;
    private final IndexDAO indexDAO;
    private final LemmaDAO lemmaDAO;

//    private final String startUrl;

//    @Override
//    public void getSiteMap(){
//
//        SiteMapImpl site = new SiteMapImpl(startUrl, startUrl);
//        ForkJoinPool forkJoinPool = new ForkJoinPool();
//        forkJoinPool.invoke(site);
//        createPages(site.getLinks());
//
//    }

    @Override
    public Page createPage(String url){

        Page page = new Page();
        page.setPath(url);
        page.setCode(getCode(url));
        page.setContent(getContent(url));
        pageDAO.savePage(page);
        generateLemms(page);
        createRank(page);
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

    public void generateLemms(Page page){
        LemmatizatorImpl lem = new LemmatizatorImpl(lemmaDAO);
        Document doc = Jsoup.parse(page.getContent());
        fieldDAO.findAllFields().forEach(field -> {
            Elements elements = doc.select(field.getSelector());
            elements.forEach(element -> {
                String text = Jsoup.parse(element.toString()).text();
                lem.saveLemms(lem.getLemms(text), page);
            });
        });
    }

    public void createRank(Page page){
        LemmatizatorImpl lem = new LemmatizatorImpl(lemmaDAO);
        HashMap<String, Float> words = new HashMap<>();
        Document doc = Jsoup.parse(page.getContent());
        fieldDAO.findAllFields().forEach(field -> {
            Elements elements = doc.select(field.getSelector());
            elements.forEach(element -> {
                String text = Jsoup.parse(element.toString()).text();
                lem.getLemms(text).forEach((word, count) -> words.merge(word, count * field.getWeight(), Float::sum));
            });
        });
        words.forEach((word, count) -> indexDAO.saveIndex(new Index(page, lemmaDAO.findLemmaByLemmaName(word).get(), count)));
    }
}
