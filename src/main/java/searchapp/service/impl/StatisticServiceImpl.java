package searchapp.service.impl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchapp.mapper.StatisticWrapper;
import searchapp.model.Statistic;
import searchapp.model.Total;
import searchapp.repository.LemmaRepository;
import searchapp.repository.PageRepository;
import searchapp.repository.SiteRepository;
import searchapp.service.StatisticService;

@Getter
@RequiredArgsConstructor
@Service
public class StatisticServiceImpl implements StatisticService {

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;

    @Override
    public StatisticWrapper getStatistic(){
        Total total = new Total();
        total.setIndexing(true);
        total.setSites(siteRepository.count());
        total.setPages(pageRepository.count());
        total.setLemmas(lemmaRepository.count());
        Statistic statistic = new Statistic();
        statistic.setDetailed(siteRepository.findAll());
        statistic.setTotal(total);
        return StatisticWrapper.builder()
                .result(true)
                .statistics(statistic)
                .build();
    }

}
