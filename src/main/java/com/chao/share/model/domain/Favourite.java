package com.chao.share.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName favourite
 */
@TableName(value ="favourite")
@Data
public class Favourite implements Serializable {
    /**
     *  id
     */
    @TableId(value = "id")
    private Long id;

    /**
     *  用户ID
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     *  文章ID
     */
    @TableField(value = "contentID")
    private String contentID;

    /**
     *  创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}