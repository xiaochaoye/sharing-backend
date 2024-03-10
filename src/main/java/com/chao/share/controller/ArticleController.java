package com.chao.share.controller;

import com.chao.share.model.domain.Article;
import com.chao.share.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  文章接口   增删改查
 *
 *  @author 超
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @PostMapping("/upload")
    public Article createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
    }

    @PutMapping("/{id}")
    public Article updateArticle(@PathVariable String id, @RequestBody Article article) {
        return articleService.updateArticle(id, article);
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable String id) {
        return articleService.getArticleById(id);
    }

}
