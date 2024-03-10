package com.chao.share.mapper;

import com.chao.share.model.domain.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author 超
 * @description 针对集合【Article(文章)】的数据库操作
 * @Entity .domain.User
 */
public interface ArticleRepository extends MongoRepository<Article, String> {
    // 可以在这里定义自定义的查询方法
}
