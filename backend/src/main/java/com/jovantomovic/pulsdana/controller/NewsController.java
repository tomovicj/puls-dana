package com.jovantomovic.pulsdana.controller;

import com.jovantomovic.pulsdana.model.News;
import com.jovantomovic.pulsdana.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/news")
@CrossOrigin
@RequiredArgsConstructor
public class NewsController {
    private final NewsService service;

    @GetMapping
    public List<News> getNews() {
        return service.getNews();
    }

    @GetMapping("/{id}")
    public News getNewsById(@PathVariable String id) {
        return service.getNewsById(id);
    }

    @GetMapping
    public List<News> getNewsBySource(@RequestParam String source) {
        return service.getNewsBySource(source);
    }
}