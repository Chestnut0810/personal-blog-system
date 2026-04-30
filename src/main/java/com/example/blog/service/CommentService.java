package com.example.blog.service;

import com.example.blog.entity.Comment;
import com.example.blog.entity.User;
import com.example.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public List<Comment> getByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId);
        comments.forEach(c -> {
            User user = userService.findById(c.getUserId());
            if (user != null) c.setUsername(user.getUsername());
        });
        return comments;
    }

    public Comment create(String content, Long articleId, Long userId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        return commentRepository.save(comment);
    }
}