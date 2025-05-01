package com.jovantomovic.pulsdana.controller;

import com.jovantomovic.pulsdana.model.NewsSource;
import com.jovantomovic.pulsdana.service.NewsSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/news-source")
@CrossOrigin
@RequiredArgsConstructor
public class NewsSourceController {
    private final NewsSourceService service;

    @GetMapping
    public List<NewsSource> getNewsSources() {
        return service.getNewsSources();
    }

    @GetMapping(path = "/{name}")
    public NewsSource getNewsSource(@PathVariable String name) {
        return service.getNewsSourceByName(name);
    }

    @PostMapping
    public NewsSource createNewsSource(@RequestBody NewsSource model) {
        return service.createNewsSource(model);
    }

    @PutMapping(path = "/{name}")
    public NewsSource updateNewsSource(@PathVariable String name, @RequestBody NewsSource model) {
        return service.updateNewsSource(name, model);
    }

    @DeleteMapping(path = "/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNewsSource(@PathVariable String name) {
        service.deleteNewsSource(name);
    }
}
