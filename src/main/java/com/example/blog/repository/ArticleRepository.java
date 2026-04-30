package com.example.blog.repository;

import com.example.blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByStatus(String status);
    List<Article> findByAuthorId(Long authorId);
}