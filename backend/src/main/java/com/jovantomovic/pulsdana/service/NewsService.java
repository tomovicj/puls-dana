package com.jovantomovic.pulsdana.service;

import com.jovantomovic.pulsdana.dto.NewsResponse;
import com.jovantomovic.pulsdana.exception.ResourceNotFoundException;
import com.jovantomovic.pulsdana.model.News;
import com.jovantomovic.pulsdana.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository repository;
    private final NewsCommentsService newsCommentsService;

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

    public News saveNews(News news) {
        return repository.save(news);
    }

    public News createNews(News model) {
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
        return saveNews(news);
    }

    public List<News> createAllNews(List<News> newsList) {
        List<News> createdNews = new ArrayList<>();
        for (News news: newsList) {
            createdNews.add(createNews(news));
        }
        return createdNews;
    }

    public NewsResponse newsToResponse(News news) {
        if (news.getDeletedAt() != null) {
            return null;
        }

        NewsResponse nr = new NewsResponse();
        nr.setId(news.getId());
        nr.setTitle(news.getTitle());
        nr.setSummary(news.getSummary());
        nr.setImageUrl(news.getImageUrl());
        nr.setPublishedAt(news.getPublishedAt());
        nr.setPostUrl(news.getPostUrl());
        nr.setSource(news.getSource());
        nr.setUpVotes(news.getUpVotes().size());
        nr.setDownVotes(news.getDownVotes().size());
        nr.setCommentCount(newsCommentsService.countComments(news.getComments()));
        return nr;
    }

    public List<NewsResponse> newsToResponse(List<News> newsList) {
        return newsList.stream()
                .map(this::newsToResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
