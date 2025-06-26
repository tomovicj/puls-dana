package com.jovantomovic.pulsdana.service;

import com.jovantomovic.pulsdana.dto.NewsCommentCreateReq;
import com.jovantomovic.pulsdana.dto.NewsCommentResponse;
import com.jovantomovic.pulsdana.exception.ResourceNotFoundException;
import com.jovantomovic.pulsdana.exception.UnauthorizedException;
import com.jovantomovic.pulsdana.model.Comment;
import com.jovantomovic.pulsdana.model.News;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsCommentsService {
    NewsService newsService;

    public List<Comment> getAllComments(String newsId) {
        News news = newsService.getNewsById(newsId);
        return news.getComments();
    }

    public Comment createComment(String newsId, NewsCommentCreateReq req, String username) {
        News news = newsService.getNewsById(newsId);

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setUsername(username);
        comment.setContent(req.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpVotes(new ArrayList<String>());
        comment.setDownVotes(new ArrayList<String>());
        comment.setReplies(new ArrayList<Comment>());
        comment.setEditedAt(null);
        comment.setDeletedAt(null);

        if (req.getReplyTo() == null) {
            news.getComments().add(comment);
        } else {
            Comment repliesTo = findCommentById(news.getComments(), req.getReplyTo());
            if (repliesTo == null) {
                throw new ResourceNotFoundException("No comment with that id on this news post");
            }

            repliesTo.getReplies().add(comment);
        }

        newsService.saveNews(news);
        return comment;
    }

    public Comment editComment(String newsId, String commentId, String username, String newContent) {
        News news = newsService.getNewsById(newsId);
        Comment comment = findCommentById(news.getComments(), commentId);
        if (comment.getDeletedAt() != null) {
            throw new ResourceNotFoundException("No comment with that id on this news post");
        }
        if (!comment.getUsername().equals(username)) {
            throw new UnauthorizedException("You're not the author of this comment");
        }

        comment.setContent(newContent);
        comment.setEditedAt(LocalDateTime.now());

        newsService.saveNews(news);
        return comment;
    }

    public void deleteComment(String newsId, String commentId, String username) {
        News news = newsService.getNewsById(newsId);
        Comment comment = findCommentById(news.getComments(), commentId);
        if (comment.getDeletedAt() != null) {
            throw new ResourceNotFoundException("No comment with that id on this news post");
        }
        if (!comment.getUsername().equals(username)) {
            throw new UnauthorizedException("You're not the author of this comment");
        }

        comment.setDeletedAt(LocalDateTime.now());
        newsService.saveNews(news);
    }

    public Comment findCommentById(List<Comment> comments, String commentId) {
        for (Comment comment : comments) {
            if (comment.getId().equalsIgnoreCase(commentId)) return comment;
            return findCommentById(comment.getReplies(), commentId);
        }
        return null;
    }

    public int countComments(List<Comment> comments) {
        int count = (int) comments.stream()
                .filter(comment -> comment.getDeletedAt() == null)
                .count();
        for (Comment comment: comments) {
            count += countComments(comment.getReplies());
        }
        return count;
    }

    public NewsCommentResponse newsCommentsToResponse(Comment comment) {
        if (comment.getDeletedAt() != null) {
            return null;
        }

        NewsCommentResponse ncr = new NewsCommentResponse();
        ncr.setId(comment.getId());
        ncr.setUsername(comment.getUsername());
        ncr.setContent(comment.getContent());
        ncr.setCreatedAt(comment.getCreatedAt());
        ncr.setUpVotes(comment.getUpVotes().size());
        ncr.setDownVotes(comment.getDownVotes().size());
        ncr.setReplies(newsCommentsToResponse(comment.getReplies()));
        return ncr;
    }

    public List<NewsCommentResponse> newsCommentsToResponse(List<Comment> comments) {
        return comments.stream()
                .map(this::newsCommentsToResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
