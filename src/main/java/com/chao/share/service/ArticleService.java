package com.chao.share.service;

import com.chao.share.model.domain.Article;

import java.util.List;

public interface ArticleService  {
    List<Article> getAllArticles();

    Article createArticle(Article article);

    void deleteArticle(String id);

    Article updateArticle(String id, Article article);

    Article getArticleById(String id);
}
