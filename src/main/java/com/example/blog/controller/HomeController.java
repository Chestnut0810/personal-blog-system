package com.example.blog.controller;

import com.example.blog.entity.Article;
import com.example.blog.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("articles", articleService.getPublishedArticles());
        model.addAttribute("user", session.getAttribute("user"));
        return "index";
    }
}