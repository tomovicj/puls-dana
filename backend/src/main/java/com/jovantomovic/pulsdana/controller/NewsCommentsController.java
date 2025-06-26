package com.jovantomovic.pulsdana.controller;

import com.jovantomovic.pulsdana.dto.NewsCommentCreateReq;
import com.jovantomovic.pulsdana.dto.NewsCommentEditReq;
import com.jovantomovic.pulsdana.dto.NewsCommentResponse;
import com.jovantomovic.pulsdana.model.Comment;
import com.jovantomovic.pulsdana.service.NewsCommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/news/{newsId}/comments")
@CrossOrigin
@RequiredArgsConstructor
public class NewsCommentsController {
    private final NewsCommentsService newsCommentsService;

    @GetMapping
    public List<NewsCommentResponse> getComments(@PathVariable String newsId) {
        List<Comment> comments = newsCommentsService.getAllComments(newsId);
        return newsCommentsService.newsCommentsToResponse(comments);
    }

    @PostMapping
    public NewsCommentResponse createNewsComment(@PathVariable String newsId, @RequestBody NewsCommentCreateReq req, Authentication auth) {
        Comment comment = newsCommentsService.createComment(newsId, req, auth.getName());
        return newsCommentsService.newsCommentsToResponse(comment);
    }

    @PostMapping("/{commentId}/edit")
    public NewsCommentResponse editNewsComment(@PathVariable String newsId, @PathVariable String commentId, Authentication auth, NewsCommentEditReq req) {
        Comment editedComment = newsCommentsService.editComment(newsId, commentId, auth.getName(), req.getContent());
        return newsCommentsService.newsCommentsToResponse(editedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteNewsComment(@PathVariable String newsId, @PathVariable String commentId, Authentication auth) {
        newsCommentsService.deleteComment(newsId, commentId, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
