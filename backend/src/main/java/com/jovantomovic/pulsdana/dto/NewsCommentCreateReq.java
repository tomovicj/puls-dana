package com.jovantomovic.pulsdana.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewsCommentCreateReq {
    private String replyTo = null;
    private String content;
}
