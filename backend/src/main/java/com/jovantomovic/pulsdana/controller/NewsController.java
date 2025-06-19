package com.jovantomovic.pulsdana.controller;

import com.jovantomovic.pulsdana.dto.NewsResponse;
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
    public List<NewsResponse> getNews(@RequestParam(required = false) String source) {
        if (source != null) {
            return service.newsListToNewsResponseList(
                    service.getNewsBySource(source)
            );
        }
        return service.newsListToNewsResponseList(
                service.getNews()
        );
    }

    @GetMapping("/{id}")
    public NewsResponse getNewsById(@PathVariable String id) {
        return new NewsResponse(service.getNewsById(id));
    }
}