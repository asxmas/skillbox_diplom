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
import searchapp.dao.FieldDAO;
import searchapp.dao.IndexDAO;
import searchapp.dao.LemmaDAO;
import searchapp.dao.impl.FieldDAOImpl;
import searchapp.dao.impl.IndexDAOImpl;
import searchapp.dao.impl.LemmaDAOImpl;
import searchapp.dao.impl.PageDAOimpl;
import searchapp.entity.Index;
import searchapp.entity.Page;
import searchapp.dao.PageDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

@Getter
@RequiredArgsConstructor
@Service
public class PageServiceImpl implements searchapp.service.PageService {

    @Autowired
    private final PageDAO pageDAO = new PageDAOimpl();
    private final FieldDAO fieldDAO = new FieldDAOImpl();
    private final IndexDAO indexDAO = new IndexDAOImpl();
    private final LemmaDAO lemmaDAO = new LemmaDAOImpl();

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

        Page page = new Page();
        page.setPath(url);
        page.setCode(getCode(url));
        page.setContent(getContent(url));
        pageDAO.savePage(page);
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

    private void createRank(Page page){
        Document doc = Jsoup.parse(page.getContent());
        fieldDAO.findAllFields().forEach(field -> {
            Elements elements = doc.select(field.getSelector());
            elements.forEach(element -> {
                String text = Jsoup.parse(element.toString()).text();
                LemmatizatorImpl lem = new LemmatizatorImpl(text);
                lem.saveLemms(lem.getLemms(), page);
                lem.getLemms().forEach((word, count)
                        -> indexDAO.saveIndex(new Index(page
                                                    , lemmaDAO.findLemmaByLemmaName(word).get()
                                                    , count * field.getWeight())));
            });
        });

    }
}
