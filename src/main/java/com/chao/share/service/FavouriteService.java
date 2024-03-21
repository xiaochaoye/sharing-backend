package com.chao.share.service;

import com.chao.share.model.domain.Article;
import com.chao.share.model.domain.Favourite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
*   @author 超
*   @description 针对表【favourite】的数据库操作Service
*
*/
public interface FavouriteService extends IService<Favourite> {
    int addFavourite(Long userId, String id);

    String deleteFavourite(Long userId, String id);

    List<Article> getFavouriteList(Long userId);

}
