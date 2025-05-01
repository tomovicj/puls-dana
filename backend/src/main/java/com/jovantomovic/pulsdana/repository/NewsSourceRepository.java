package com.jovantomovic.pulsdana.repository;

import com.jovantomovic.pulsdana.model.NewsSource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsSourceRepository extends MongoRepository<NewsSource, String> {
    List<NewsSource> findByDeletedAtIsNull();
    Optional<NewsSource> findByNameAndDeletedAtIsNull(String name);
    List<NewsSource> findByIsEnabledIsTrueAndDeletedAtIsNull();
}
