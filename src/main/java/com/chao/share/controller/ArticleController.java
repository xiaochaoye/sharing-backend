package com.chao.share.controller;

import com.chao.share.common.BaseResponse;
import com.chao.share.common.ErrorCode;
import com.chao.share.common.ResultUtils;
import com.chao.share.exception.BusinessException;
import com.chao.share.model.domain.Article;
import com.chao.share.model.domain.Favourite;
import com.chao.share.model.domain.User;
import com.chao.share.service.ArticleService;
import com.chao.share.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    private FavouriteService favouriteService;

    /**
     *  文章列表
     */
    @GetMapping("/cards")
    @ResponseBody
    public BaseResponse<List<Article>> getAllArticles() {
        List<Article> allArticles = articleService.getAllArticles();
        return ResultUtils.success(allArticles);
    }

    /**
     *  创建文章
     *  @param article
     *  @return uploadArticle
     */
    @PostMapping("/upload")
    public BaseResponse<Article> createArticle(@RequestBody Article article) {
        Article uploadArticle = articleService.createArticle(article);
        return ResultUtils.success(uploadArticle);
    }

    /**
     *  删除文章
     *  @param id
     *  @param request
     *  @return
     */
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

    /**
     *  更新文章
     *  @param id
     *  @param article
     *  @return
     */
    @PostMapping("/update")
    public BaseResponse<Article> updateArticle(@RequestParam("id") String id, @RequestBody Article article) {
        Article updateArticle = articleService.updateArticle(id, article);
        return ResultUtils.success(updateArticle);
    }

    /**
     *  获取文章
     *  @param id
     *  @return
     */
    @GetMapping("/read")
    public BaseResponse<Article> getArticleById(@RequestParam("id") String id) {
        Article articleById = articleService.getArticleById(id);
        return ResultUtils.success(articleById);
    }

    /**
     *  点赞文章
     *  @param id
     *  @return
     */
    @GetMapping("/like")
    public BaseResponse<Article> likeArticle(@RequestParam("id") String id) {
        Article likeArticle = articleService.getArticleById(id);
        if (likeArticle != null) {
            System.out.println("当前点赞数" + likeArticle.getLikeCount());
            likeArticle.setLikeCount(likeArticle.getLikeCount() + 1);
            articleService.updateArticle(id, likeArticle);
        }
        return ResultUtils.success(likeArticle);
    }

    /**
     *  取消点赞文章
     *  @param id
     *  @return
     */
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

    /**
     *  文章标题搜索
     *  @param title
     *  @return
     */
    @GetMapping("/search/title")
    public BaseResponse<List<Article>> getSearchTitle(@RequestParam("keyword") String title) {
        List<Article> list = articleService.searchArticlesByTitle(title);
        return ResultUtils.success(list);
    }

    /**
     *  文章内容搜索
     *  @param content
     *  @return
     */
    @GetMapping("/search/content")
    public BaseResponse<List<Article>> getSearchContent(@RequestParam("keyword") String content) {
        List<Article> list = articleService.searchArticlesByContent(content);
        return ResultUtils.success(list);
    }

    /**
     *  添加收藏
     *  @param favourite
     *  @return
     */
    @PostMapping("/favourite/set")
    public BaseResponse<Integer> setFavourite(@RequestBody Favourite favourite) {
        String id = favourite.getContentID();
        Article article = articleService.getArticleById(id);
        if (article != null) {
            article.setCollectCount(article.getCollectCount() + 1);
        }
        int added = favouriteService.addFavourite(favourite.getUserId(), favourite.getContentID());
        return ResultUtils.success(added);
    }

    /**
     *  获取收藏列表
     *  @param userId
     *  @return
     */
    @GetMapping("/favourite/get")
    public BaseResponse<List<Article>> getFavourite(@RequestParam("userId") Long userId) {
        List<Article> tempList = favouriteService.getFavouriteList(userId);
        System.out.println(tempList);
        return ResultUtils.success(tempList);
    }

    /**
     *  删除收藏
     *  @param favourite
     *  @return
     */
    @PostMapping("/favourite/remove")
    public BaseResponse<String> removeFavourite(@RequestBody Favourite favourite) {
        String id = favourite.getContentID();
        Article article = articleService.getArticleById(id);
        if (article != null) {
            article.setCollectCount(article.getCollectCount() - 1);
        }
        String removed = favouriteService.deleteFavourite(favourite.getUserId(), favourite.getContentID());
        return ResultUtils.success(removed);
    }

    /**
     *  获取用户自己的文章列表
     *  @param request
     *  @return
     */
    @GetMapping("/mine")
    public BaseResponse<List<Article>> getMine(HttpServletRequest request) {
        Object object = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) object;
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        Long authorId = user.getId();
        List<Article> byId = articleService.getArticlesByUser(authorId);
        return ResultUtils.success(byId);
    }
}
