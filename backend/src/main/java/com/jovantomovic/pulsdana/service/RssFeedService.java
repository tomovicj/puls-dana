package com.jovantomovic.pulsdana.service;

import com.jovantomovic.pulsdana.model.News;
import com.jovantomovic.pulsdana.model.NewsSource;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RssFeedService {
    private final NewsService newsService;
    private final NewsSourceService newsSourceService;

    public List<News> fetchNewsFromRss(NewsSource newsSource) {
        List<News> newsList = new ArrayList<>();

        URL rssFeedUrl;
        try {
            URI uri = new URI(newsSource.getRssFeedUrl());
            rssFeedUrl = uri.toURL();
        } catch (Exception e) {
            e.printStackTrace();
            return newsList;
        }

        try (XmlReader reader = new XmlReader(rssFeedUrl.openStream())) {
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (SyndEntry entry : feed.getEntries()) {
                News news = new News();
                news.setTitle(entry.getTitle());
                news.setSummary(entry.getDescription() != null ? entry.getDescription().getValue() : "");
                if (entry.getPublishedDate() != null) {
                    news.setPublishedAt(entry.getPublishedDate()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime());
                }
                if (!entry.getEnclosures().isEmpty()) {
                    news.setImageUrl(entry.getEnclosures().getFirst().getUrl());
                }
                news.setPostUrl(entry.getLink());
                news.setSource(newsSource.getName());

                news.setUpVotes(new ArrayList<>());
                news.setDownVotes(new ArrayList<>());
                news.setComments(new ArrayList<>());

                newsList.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }

    // All news must be from the same source
    public List<News> filterOutOldNews(List<News> allNews) {
        List<News> newNews = new ArrayList<>();
        if (allNews.isEmpty()) {
            return newNews;
        }

        String source = allNews.getFirst().getSource();

        List<News> oldNews = newsService.getNewsBySource(source);

        for (News news: allNews) {
            boolean isOldNews = oldNews.stream().anyMatch(n -> n.getPostUrl().equalsIgnoreCase(news.getPostUrl()));
            if (isOldNews) {
                break;
            }
            newNews.add(news);
        }
        return newNews;
    }

    // Get new news every 15 min
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void getNewNews() {
        List<NewsSource> newsSources = newsSourceService.getEnabledNewsSources();

        for (NewsSource ns: newsSources) {
            List<News> allNews = fetchNewsFromRss(ns);
            List<News> newNews = filterOutOldNews(allNews);
            newsService.createAllNews(newNews);
        }
    }
}
