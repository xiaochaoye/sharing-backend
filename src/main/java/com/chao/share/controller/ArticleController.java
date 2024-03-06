package com.chao.share.controller;

import com.chao.share.service.ArticleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *  文章接口
 *
 *  @author 超
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    public ArticleService articleService;
}
