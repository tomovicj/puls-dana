package com.jovantomovic.pulsdana.service;

import com.jovantomovic.pulsdana.dto.NewsResponse;
import com.jovantomovic.pulsdana.exception.ResourceNotFoundException;
import com.jovantomovic.pulsdana.model.News;
import com.jovantomovic.pulsdana.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository repository;

    public List<NewsResponse> newsListToNewsResponseList(List<News> newsList) {
        return newsList.stream()
                .map(NewsResponse::new)
                .collect(Collectors.toList());
    }

    public List<News> getNews() {
        return repository.findByDeletedAtIsNull();
    }

    public News getNewsById(String id) {
        return repository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
    }

    public List<News> getNewsBySource(String sourceName) {
        return repository.findBySourceAndDeletedAtIsNull(sourceName);
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

    public List<News> saveAllNews(List<News> newsList) {
        List<News> savedNews = new ArrayList<>();
        for (News news: newsList) {
            savedNews.add(saveNews(news));
        }
        return savedNews;
    }
}
