package com.chao.share.service;

import com.chao.share.model.domain.Favourite;
import com.baomidou.mybatisplus.extension.service.IService;

/**
*   @author 超
*   @description 针对表【favourite】的数据库操作Service
*
*/
public interface FavouriteService extends IService<Favourite> {
    int addFavourite(Long userId, String id);

    String deleteFavourite(Long userId, String id);

}
