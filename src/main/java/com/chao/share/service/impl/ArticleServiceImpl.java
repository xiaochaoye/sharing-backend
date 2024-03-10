package com.chao.share.service.impl;

import com.chao.share.mapper.ArticleRepository;
import com.chao.share.model.domain.Article;
import com.chao.share.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void deleteArticle(String id) {
        articleRepository.deleteById(id);
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