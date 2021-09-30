package searchapp.service.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import searchapp.entity.Page;
import searchapp.repository.PageRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Getter
public class PageServiceImpl extends RecursiveAction {
    private static PageRepository pageRepository;

    @NonNull
    private static String startUrl;
    private String url;

    public PageServiceImpl(String url){
        this.url = url;
    }

    public PageServiceImpl(String startUrl, String url){
        this.url = url;
        PageServiceImpl.startUrl = startUrl;
    }

    @Override
    protected void compute() {
        Set<PageServiceImpl> subTask = new HashSet<>();
        getChilds(subTask);
        for(PageServiceImpl pageService : subTask){
            pageService.join();
        }
    }

    public void getChilds(Set<PageServiceImpl> subTask){
        try {
            //todo определить timeout() и maxBodySize()
            Document doc = Jsoup.connect(url).maxBodySize(0).get();
            Elements elements = doc.select("a");
            elements.forEach(element -> {
                String newLink = element.attr("abs:href");
                if(!newLink.isEmpty() && newLink.startsWith(startUrl) && !newLink
                        .contains("#")){
                    PageServiceImpl pageService = new PageServiceImpl(newLink);
                    try {
                        Thread.sleep((long) (Math.random()*5000 + 500));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    pageService.fork();
                    subTask.add(pageService);
                    createPage(newLink);
                }
            });
        } catch (IOException e){
        }
    }

    public Page createPage(String url){
        Page page = new Page();
        page.setPath(url);
        page.setCode(getCode(url));
        page.setContent(getContent(url));
        pageRepository.save(page);
        return page;
    }

    private int getCode(String link){
        int code = 404;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    public String getContent(String link){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(link);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            BufferedReader in = new BufferedReader(reader);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public boolean checkLink(String url){
        Pattern pattern = Pattern.compile(url + ".+/$");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }


}
