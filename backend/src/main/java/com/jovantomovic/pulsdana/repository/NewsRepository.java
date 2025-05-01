package com.jovantomovic.pulsdana.repository;

import com.jovantomovic.pulsdana.model.News;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends MongoRepository<News, String> {
    List<News> findBySource(String sourceName);
}
