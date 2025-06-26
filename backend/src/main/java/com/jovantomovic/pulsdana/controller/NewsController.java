package com.jovantomovic.pulsdana.controller;

import com.jovantomovic.pulsdana.dto.NewsResponse;
import com.jovantomovic.pulsdana.exception.ResourceNotFoundException;
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
    public List<NewsResponse> getNews(@RequestParam(required = false) String source) {
        if (source != null) {
            return service.newsToResponse(
                    service.getNewsBySource(source)
            );
        }
        return service.newsToResponse(
                service.getNews()
        );
    }

    @GetMapping("/{newsId}")
    public NewsResponse getNewsById(@PathVariable String newsId) {
        News news = service.getNewsById(newsId);
        NewsResponse response = service.newsToResponse(news);

        if (response == null) {
            throw new ResourceNotFoundException("No news with that id");
        }

        return response;
    }
}