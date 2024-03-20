package com.chao.share.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.share.model.domain.Favourite;
import com.chao.share.service.FavouriteService;
import com.chao.share.mapper.FavouriteMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
*   @author 超
*   @description 针对表【favourite】的数据库操作Service实现
*
*/
@Service
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite>
    implements FavouriteService{

    @Resource
    private FavouriteMapper favouriteMapper;

    @Override
    public int addFavourite(Long userId, String id) {
        Favourite favourite = new Favourite();
        favourite.setUserId(userId);
        favourite.setContentID(id);
        favourite.setCreateTime(new Date());
     // 使用Mybatis-Plus提供的save方法
     // favouriteMapper.insert(favourite);
        boolean save = this.save(favourite);
        if (save) {
            return 1;
        }
        return 0;
    }

    @Override
    public String deleteFavourite(Long userId, String id) {
        favouriteMapper.deleteByContentId(userId, id);
        return "移除收藏成功";
    }
}




