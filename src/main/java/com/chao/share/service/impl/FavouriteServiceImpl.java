package com.chao.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.share.common.ErrorCode;
import com.chao.share.exception.BusinessException;
import com.chao.share.model.domain.Article;
import com.chao.share.model.domain.Favourite;
import com.chao.share.service.FavouriteService;
import com.chao.share.mapper.FavouriteMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public int addFavourite(Long userId, String id) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("contentID", id);
        Favourite one = favouriteMapper.selectOne(queryWrapper);
        if (one != null) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "已收藏过该文章");
        }
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

    @Override
    public List<Article> getFavouriteList(Long userId) {
        LambdaQueryWrapper<Favourite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favourite::getUserId, userId);
        List<String> list = this.list(queryWrapper).stream().map(Favourite::getContentID).collect(Collectors.toList());
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(list));
        return mongoTemplate.find(query, Article.class);
    }
}




