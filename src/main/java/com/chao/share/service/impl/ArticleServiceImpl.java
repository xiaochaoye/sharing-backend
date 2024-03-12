package com.chao.share.service.impl;

import com.chao.share.common.ErrorCode;
import com.chao.share.common.UserConstant;
import com.chao.share.exception.BusinessException;
import com.chao.share.mapper.ArticleRepository;
import com.chao.share.model.domain.Article;
import com.chao.share.model.domain.User;
import com.chao.share.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(String id, User user, Article article) {
        if (user == null || user.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        // 检查用户权限，确保用户有权限删除文章
        if (user.getId().equals(article.getAuthorId()) || isAdminLogin(user)) {
            // 删除文章
            articleRepository.deleteById(id);
        } else {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限删除文章");
        }
    }

    private boolean isAdminLogin(User user) {
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    @Override
    public Article updateArticle(String id, Article article) {
        article.setId(id);
        return articleRepository.save(article);
    }

    @Override
    public Article getArticleById(String id) {
        return articleRepository.findById(id).orElse(null);
    }
}