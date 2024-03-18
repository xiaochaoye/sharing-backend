package com.chao.share.controller;

import com.chao.share.common.BaseResponse;
import com.chao.share.common.ErrorCode;
import com.chao.share.common.ResultUtils;
import com.chao.share.exception.BusinessException;
import com.chao.share.model.domain.Article;
import com.chao.share.model.domain.User;
import com.chao.share.service.ArticleService;
import com.chao.share.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.chao.share.common.UserConstant.USER_LOGIN_STATE;

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

    @Resource
    private UserService userService;

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

    @GetMapping("/delete")
    public BaseResponse<String> deleteArticle(@RequestParam("id") String id, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user== null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }
        Article article = articleService.getArticleById(id);
        if (article != null) {
            articleService.deleteArticle(id, user, article);
            return ResultUtils.success("删除成功");
        } else {
            throw new BusinessException(ErrorCode.FILE_ERROR, "文章不存在");
        }
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

    @GetMapping("/like")
    public BaseResponse<Article> likeArticle(@RequestParam("id") String id) {
        Article likeArticle = articleService.getArticleById(id);
        System.out.println("文章内容：" + likeArticle);
        if (likeArticle != null) {
            System.out.println("当前点赞数" + likeArticle.getLikeCount());
            likeArticle.setLikeCount(likeArticle.getLikeCount() + 1);
            articleService.updateArticle(id, likeArticle);
        }
        return ResultUtils.success(likeArticle);
    }

    @GetMapping("/dislike")
    public BaseResponse<Article> disLikeArticle(@RequestParam("id") String id) {
        Article likeArticle = articleService.getArticleById(id);
        System.out.println("文章内容：" + likeArticle);
        if (likeArticle != null) {
            System.out.println("当前点赞数" + likeArticle.getLikeCount());
            likeArticle.setLikeCount(likeArticle.getLikeCount() - 1);
            articleService.updateArticle(id, likeArticle);
        }
        return ResultUtils.success(likeArticle);
    }

    @GetMapping("/favourite")
    public BaseResponse<Article> getFavourite(@RequestParam("id") String id) {
        return null;
    }

    @GetMapping("/search")
    public BaseResponse<Article> getSearch(@RequestParam("id") String id) {
        return null;
    }

    @GetMapping("/mine")
    public BaseResponse<Article> getMine(@RequestParam("id") String id) {
        return null;
    }
}
