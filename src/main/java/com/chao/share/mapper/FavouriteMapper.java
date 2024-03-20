package com.chao.share.mapper;

import com.chao.share.model.domain.Favourite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
*   @author 超
*   @description 针对表【favourite(收藏表)】的数据库操作Mapper
*   @Entity com.chao.share.model.domain.Favourite
*/
public interface FavouriteMapper extends BaseMapper<Favourite> {
    void deleteByContentId(@Param("userId") Long userId, @Param("contentId") String contentId);
}




