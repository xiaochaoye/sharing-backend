package com.chao.share.model.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

/**
 *  @author 超
 *
 *  集合 id, 文章标题 title, 文章作者 author, 作者id authorId, 文章描述 description,
 *  文章封面 cover, 文章内容 content, 点赞按钮是否可用 isDisabled,
 *  点赞和取消点赞次数 clickCount, 文章点赞次数 likeCount
 *
 */
@Document(collection = "articles")
public class Article {

    @Setter
    @Getter
    @Id
    private String id;

    @Setter
    @Getter
    private String title;

    @Setter
    @Getter
    private String author;

    @Setter
    @Getter
    private String authorId;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String cover;

    @Setter
    @Getter
    private String content;

    private boolean isDisabled;

    @Setter
    @Getter
    private int clickCount;

    @Setter
    @Getter
    private int likeCount;

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

}
