package com.example.blog.service;

import com.example.blog.entity.Article;
import com.example.blog.entity.User;
import com.example.blog.repository.ArticleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    public List<Article> getPublishedArticles() {
        List<Article> articles = articleRepository.findByStatus("PUBLISHED");
        articles.forEach(this::setAuthorName);
        return articles;
    }

    public Article getById(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article != null) setAuthorName(article);
        return article;
    }

    public Article create(String title, String content, Long authorId) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthorId(authorId);
        article.setStatus("PENDING");
        return articleRepository.save(article);
    }

    public Article update(Long id, String title, String content, Long userId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));
        if (!article.getAuthorId().equals(userId)) {
            throw new RuntimeException("无权限");
        }
        article.setTitle(title);
        article.setContent(content);
        article.setUpdatedAt(LocalDateTime.now());
        article.setStatus("PENDING");
        return articleRepository.save(article);
    }

    public void delete(Long id, Long userId, boolean isAdmin) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));
        if (!isAdmin && !article.getAuthorId().equals(userId)) {
            throw new RuntimeException("无权限");
        }
        articleRepository.deleteById(id);
    }

    public List<Article> getMyArticles(Long userId) {
        List<Article> articles = articleRepository.findByAuthorId(userId);
        articles.forEach(this::setAuthorName);
        return articles;
    }

    public List<Article> getPendingArticles() {
        List<Article> articles = articleRepository.findByStatus("PENDING");
        articles.forEach(this::setAuthorName);
        return articles;
    }

    public void approve(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));
        article.setStatus("PUBLISHED");
        articleRepository.save(article);
    }

    public void reject(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文章不存在"));
        article.setStatus("REJECTED");
        articleRepository.save(article);
    }

    private void setAuthorName(Article article) {
        User author = userService.findById(article.getAuthorId());
        if (author != null) article.setAuthorName(author.getUsername());
    }
}