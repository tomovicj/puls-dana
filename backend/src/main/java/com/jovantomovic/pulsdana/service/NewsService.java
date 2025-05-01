package com.jovantomovic.pulsdana.service;

import com.jovantomovic.pulsdana.exception.ResourceNotFoundException;
import com.jovantomovic.pulsdana.model.News;
import com.jovantomovic.pulsdana.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository repository;

    public List<News> getNews() {
        return repository.findAll();
    }

    public News getNewsById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
    }

    public List<News> getNewsBySource(String sourceName) {
        return repository.findBySource(sourceName);
    }

    public News saveNews(News model) {
        News news = new News();
        news.setTitle(model.getTitle());
        news.setSummary(model.getSummary());
        news.setImageUrl(model.getImageUrl());
        news.setPublishedAt(model.getPublishedAt());
        news.setSource(model.getSource());
        news.setPostUrl(model.getPostUrl());
        news.setComments(new ArrayList<>());
        news.setUpVotes(new ArrayList<>());
        news.setDownVotes(new ArrayList<>());
        return repository.save(news);
    }
}
