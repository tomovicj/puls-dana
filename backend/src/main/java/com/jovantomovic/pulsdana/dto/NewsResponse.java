package com.jovantomovic.pulsdana.dto;

import com.jovantomovic.pulsdana.model.Comment;
import com.jovantomovic.pulsdana.model.News;
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
public class NewsResponse {
    private String id;
    private String title;
    private String summary;
    private String imageUrl;
    private LocalDateTime publishedAt;
    private String postUrl;
    private String source;
    private Integer upVotes;
    private Integer downVotes;
    private Integer commentCount;
}
