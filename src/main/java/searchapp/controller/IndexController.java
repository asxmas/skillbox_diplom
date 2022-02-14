package searchapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchapp.mapper.IndexWrapper;
import searchapp.service.PageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class IndexController {

    private final PageService pageService;

    @GetMapping("startIndexing")
    public ResponseEntity<IndexWrapper> startIndexing() { return ResponseEntity.ok(pageService.getSiteMaps()); }

    @GetMapping("stopIndexing")
    public ResponseEntity<IndexWrapper> stopIndexing() { return ResponseEntity.ok(pageService.stopIndexing()); }

    @PostMapping("indexPage")
    //todo: сделать проверку на предмет наличия в application.yaml
    public ResponseEntity<IndexWrapper> indexPage(@RequestParam String url){ return ResponseEntity.ok(pageService.getSiteMap(url)); }



}
