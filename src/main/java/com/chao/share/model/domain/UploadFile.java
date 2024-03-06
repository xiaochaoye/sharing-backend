package com.chao.share.model.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.Binary;

import java.time.LocalDateTime;

@Data
@Document
@Accessors(chain = true)
public class UploadFile {

    @Id
    private String id;

    /**
     *  文件名
     */
    private String fileName;

    /**
     *  上传时间
     */
    private LocalDateTime uploadTime;

    /**
     *  文件内容
     */
    private Binary content;

    /**
     *  文件类型
     */
    private String contentType;

    /**
     *  文件大小
     */
    private long size;

}
