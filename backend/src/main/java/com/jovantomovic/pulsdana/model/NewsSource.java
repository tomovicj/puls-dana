package com.jovantomovic.pulsdana.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class NewsSource {
    @Id
    private String name;
    private String url;
    private String rssFeedUrl;
    private String iconUrl;
    private Boolean isEnabled;
    private LocalDateTime deletedAt;
}
