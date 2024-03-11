package com.chao.share.controller;

import com.chao.share.common.BaseResponse;
import com.chao.share.common.ResultUtils;
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

    @GetMapping("/cards")
    @ResponseBody
    public BaseResponse<List<Article>> getAllArticles() {
        List<Article> allArticles = articleService.getAllArticles();
        return ResultUtils.success(allArticles);
    }

    @PostMapping("/upload")
    public BaseResponse<Article> createArticle(@RequestBody Article article) {
        Article uploadArticle = articleService.createArticle(article);
        return ResultUtils.success(uploadArticle);
    }

    @DeleteMapping("/delete")
    public void deleteArticle(@RequestParam("id") String id) {
        articleService.deleteArticle(id);
    }

    @PostMapping("/update")
    public BaseResponse<Article> updateArticle(@RequestParam("id") String id, @RequestBody Article article) {
        Article updateArticle = articleService.updateArticle(id, article);
        return ResultUtils.success(updateArticle);
    }

    @GetMapping("/read")
    public BaseResponse<Article> getArticleById(@RequestParam("id") String id) {
        Article articleById = articleService.getArticleById(id);
        return ResultUtils.success(articleById);
    }

    @PostMapping("/like")
    public BaseResponse<Article> likeArticle(@RequestParam("id") String id) {
        Article likeArticle = articleService.getArticleById(id);
        if (likeArticle != null) {
            likeArticle.setLikeCount(likeArticle.getLikeCount() + 1);
            articleService.updateArticle(id, likeArticle);
        }
        return ResultUtils.success(likeArticle);
    }
}
