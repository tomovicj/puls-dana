package com.jovantomovic.pulsdana.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comment {
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private List<String> upVotes;
    private List<String> downVotes;
    private List<Comment> replies;
    private LocalDateTime deletedAt;
}
