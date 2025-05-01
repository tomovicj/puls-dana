package com.jovantomovic.pulsdana.service;

import com.jovantomovic.pulsdana.exception.ResourceNotFoundException;
import com.jovantomovic.pulsdana.model.NewsSource;
import com.jovantomovic.pulsdana.repository.NewsSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsSourceService {
    private final NewsSourceRepository repository;

    public List<NewsSource> getNewsSources() {
        return repository.findByDeletedAtIsNull();
    }

    public NewsSource getNewsSourceByName(String name) {
        return repository.findByNameAndDeletedAtIsNull(name)
                .orElseThrow(() -> new ResourceNotFoundException("News source not found"));
    }

    public List<NewsSource> getEnabledNewsSources() {
        return repository.findByIsEnabledIsTrueAndDeletedAtIsNull();
    }

    public NewsSource createNewsSource(NewsSource model) {
        NewsSource ns = new NewsSource();
        ns.setName(model.getName());
        ns.setUrl(model.getUrl());
        ns.setIconUrl(model.getIconUrl());
        ns.setRssFeedUrl(model.getRssFeedUrl());
        ns.setIsEnabled(true);
        return repository.save(ns);
    }

    public NewsSource updateNewsSource(String name, NewsSource model) {
        NewsSource ns = getNewsSourceByName(name);
        ns.setUrl(model.getUrl());
        ns.setIconUrl(model.getIconUrl());
        ns.setRssFeedUrl(model.getRssFeedUrl());
        ns.setIsEnabled(model.getIsEnabled());
        return repository.save(ns);
    }

    public void deleteNewsSource(String name) {
        NewsSource ns = getNewsSourceByName(name);
        ns.setDeletedAt(LocalDateTime.now());
        repository.save(ns);
    }
}
