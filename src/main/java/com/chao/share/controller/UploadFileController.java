package com.chao.share.controller;

import com.chao.share.common.BaseResponse;
import com.chao.share.common.ErrorCode;
import com.chao.share.common.ResultUtils;
import com.chao.share.exception.BusinessException;
import com.chao.share.model.domain.UploadFile;
import com.chao.share.model.domain.User;
import com.chao.share.service.UserService;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/file")
public class UploadFileController {

    private MongoTemplate mongoTemplate;

    @Resource
    private UserService userService;

    private GridFsTemplate gridFsTemplate;

    /**
     *  图片上传
     */
    // todo 部署到服务器时需要更改返回的路径名
    @PostMapping("/uploadImage")
    @ResponseBody
    public BaseResponse<String> uploadImage(@RequestParam(value = "image") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }
        if (!file.getContentType().startsWith("image")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传图片文件");
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
            String serverPath = request.getScheme() + "://" + request.getServerName();
            int port = request.getServerPort();
            String contextPath = request.getContextPath();
            String imageUrl = serverPath + ":" + port + contextPath + "/file/image/" + saveFile.getId();
            return ResultUtils.success(imageUrl);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_ERROR, "文件上传失败");
        }
    }

    /**
     *  获取图片链接当头像，需要登录时才能上传头像
     */
    @PostMapping("/uploadAvatar")
    @ResponseBody
    public BaseResponse<String> uploadAvatar(@RequestParam(value = "image") MultipartFile file, HttpServletRequest request) {
        User currentUser = userService.getLoginUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "当前用户未登录");
        }
        if (!file.getContentType().startsWith("image")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传图片文件");
        }
        try {
            ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            String serverPath = request.getScheme() + "://" + request.getServerName();
            int port = request.getServerPort();
            String contextPath = request.getContextPath();
            String imageUrl = serverPath + ":" + port + contextPath + "/file/avatar/" + objectId;
            // todo update到用户表
            currentUser.setAvatarUrl(imageUrl);
            userService.updateUser(currentUser, currentUser);
        }catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_ERROR, "文件上传失败");
        }
        return null;
    }

    /**
     *  获取图片给文章中回显和作为文章封面
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
        if (file != null) {
            headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        }
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    /**
     *  获取图片给用户头像回显
     */
    @GetMapping(value = "/avatar/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> avatar(@PathVariable String id) {
        // 获取图片数据 db.sharing.files.find({_id: ObjectId("65eaf432e93e540340bfb93c")})
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        if (gridFSFile == null) {
            return ResponseEntity.notFound().build();
        }
        // 获取图片资源
        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
        try {
            byte[] imageData = StreamUtils.copyToByteArray(resource.getInputStream());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(resource.getContentType()));
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            // 处理异常情况，比如无法读取文件等
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
