package com.example.blog.controller;

import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ArticleService articleService;

    public AdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/review")
    public String reviewPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        model.addAttribute("articles", articleService.getPendingArticles());
        model.addAttribute("user", user);
        return "admin-review";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        articleService.approve(id);
        return "redirect:/admin/review";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }
        articleService.reject(id);
        return "redirect:/admin/review";
    }
}