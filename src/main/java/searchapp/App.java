package searchapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import searchapp.entity.Page;
import searchapp.service.impl.PageServiceImpl;

import java.util.concurrent.ForkJoinPool;

@Slf4j
public class App {
    public static void main(String[] args) {
        String startUrl = "http://www.playback.ru/";

        PageServiceImpl site = new PageServiceImpl(startUrl, startUrl);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(site);

        System.out.println("end");
    }
}
