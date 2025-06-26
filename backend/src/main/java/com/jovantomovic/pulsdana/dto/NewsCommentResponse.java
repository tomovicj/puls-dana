package com.jovantomovic.pulsdana.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewsCommentResponse {
    private String id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private Integer upVotes;
    private Integer downVotes;
    private List<NewsCommentResponse> replies;
}
