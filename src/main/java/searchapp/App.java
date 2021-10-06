package searchapp;

import lombok.extern.slf4j.Slf4j;
import searchapp.service.impl.PageServiceImpl;

@Slf4j
public class App {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String startUrl = "https://volochek.life/";
        PageServiceImpl pageService = new PageServiceImpl(startUrl);
        pageService.getSiteMap();

        System.out.println(System.currentTimeMillis() - start);

    }


}
