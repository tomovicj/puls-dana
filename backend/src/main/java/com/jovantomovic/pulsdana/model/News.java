package com.jovantomovic.pulsdana.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class News {
    @Id
    private String id;
    private String title;
    private String summary;
    private LocalDateTime publishedAt;
    private String imageUrl;
    private String postUrl;
    private String source;

    private List<String> upVotes;
    private List<String> downVotes;
    private List<Comment> comments;

    private LocalDateTime deletedAt;
}
