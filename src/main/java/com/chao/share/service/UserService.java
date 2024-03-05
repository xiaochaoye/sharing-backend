package com.chao.share.service;

import com.chao.share.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author wangchao
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-02-05 11:04:01
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    int userLogout(HttpServletRequest request);

    User getSafetyUser(User user);


    /**
     * 是否为管理员(查询时用)
     */
    boolean isAdminSearch(HttpServletRequest request);

    /**
     * 是否为管理员（）
     */
    boolean isAdminLogin(User loginUser);

    User getLoginUser(HttpServletRequest request);

    int updateUser(User user, User loginUser);

//    String uploadAvatar(MultipartFile file, User loginUser);

    String uploadAvatar(MultipartFile file);
}
