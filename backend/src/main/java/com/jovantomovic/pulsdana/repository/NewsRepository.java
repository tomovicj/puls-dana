package com.jovantomovic.pulsdana.repository;

import com.jovantomovic.pulsdana.model.News;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends MongoRepository<News, String> {
    List<News> findByDeletedAtIsNull();
    Optional<News> findByIdAndDeletedAtIsNull(String id);
    List<News> findBySourceAndDeletedAtIsNull(String sourceName);
}
