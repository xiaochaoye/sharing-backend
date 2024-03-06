package com.chao.share.controller;

import com.chao.share.common.BaseResponse;
import com.chao.share.common.ErrorCode;
import com.chao.share.common.ResultUtils;
import com.chao.share.exception.BusinessException;
import com.chao.share.model.domain.UploadFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/file")
public class UploadFileController {

    private MongoTemplate mongoTemplate;

    /**
     *  图片上传
     */
    @PostMapping("/uploadImage")
    @ResponseBody
    public BaseResponse<String> uploadImage(@RequestParam(value = "image") MultipartFile file) {
        if (file.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }
        try {
//            ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
//            return "http://localhost:8080/files/" + objectId;
            String filename = file.getOriginalFilename();
            UploadFile uploadFile = new UploadFile()
                    .setFileName(filename)
                    .setUploadTime(LocalDateTime.now())
                    .setContent(new Binary(file.getBytes()))
                    .setContentType(file.getContentType())
                    .setSize(file.getSize());
            UploadFile saveFile = mongoTemplate.save(uploadFile);
//            return "http://localhost:8080/file/image" + saveFile.getId();
            return ResultUtils.success("http://localhost:8080/file/image/" + saveFile.getId());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_ERROR, "文件上传失败");
        }
    }

    /**
     *  获取图片
     */
    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> image(@PathVariable String id) {
        byte[] data = null;
        UploadFile file = mongoTemplate.findById(id, UploadFile.class);
        if (file != null) {
            data = file.getContent().getData();
        }
        // 设置图片的 Content-Type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}
