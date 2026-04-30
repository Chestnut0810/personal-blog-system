package com.example.blog.controller;

import com.example.blog.entity.Article;
import com.example.blog.entity.Comment;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        Article article = articleService.getById(id);
        if (article == null) return "redirect:/";
        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.getByArticleId(id));
        model.addAttribute("user", session.getAttribute("user"));
        return "article-detail";
    }

    @GetMapping("/new")
    public String newArticle(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        return "article-form";
    }

    @PostMapping("/new")
    public String create(@RequestParam String title, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        articleService.create(title, content, user.getId());
        return "redirect:/articles/my";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Article article = articleService.getById(id);
        if (article == null || !article.getAuthorId().equals(user.getId())) {
            return "redirect:/";
        }
        model.addAttribute("article", article);
        return "article-form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @RequestParam String title,
                         @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        articleService.update(id, title, content, user.getId());
        return "redirect:/articles/my";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        articleService.delete(id, user.getId(), "ADMIN".equals(user.getRole()));
        return "redirect:/articles/my";
    }

    @GetMapping("/my")
    public String myArticles(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("articles", articleService.getMyArticles(user.getId()));
        model.addAttribute("user", user);
        return "my-articles";
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        commentService.create(content, id, user.getId());
        return "redirect:/articles/" + id;
    }
}