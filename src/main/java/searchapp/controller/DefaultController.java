package searchapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import searchapp.mapper.StatisticWrapper;
import searchapp.service.StatisticService;

@Controller
@RequiredArgsConstructor
public class DefaultController {

    private final StatisticService statisticService;

    @GetMapping("/admin")
    public String index(){
        return "index";
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticWrapper> getStatistics(){ return ResponseEntity.ok(statisticService.getStatistic()); }

}
