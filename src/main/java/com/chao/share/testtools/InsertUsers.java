package com.chao.share.testtools;

import com.chao.share.model.domain.User;
import com.chao.share.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 导入用户任务
 *
 * @author 超
 */
@Component
public class InsertUsers {

    @Resource
    private UserService userService;

    /**
     * 批量插入假用户
     */
    //不插入假数据的时候就注释掉下面那句话
//   @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int j = 0;
        List<CompletableFuture<Void>> list = new ArrayList<>();
        final int INSERT_NUM = 1000;
        int batchSize = 50;
        for (int i = 0; i < (INSERT_NUM / batchSize); i++){
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();
                user.setUsername("假数据");
                user.setUserAccount("fakedata");
                user.setAvatarUrl("https://pic2.zhimg.com/v2-2387649d9de0ef6b74aac0f69fbc8b59_r.jpg");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("123");
                user.setEmail("123@qq.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                userList.add(user);
                if (j % batchSize == 0) {
                    break;
                }
            }
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
                System.out.println("threadName；" + Thread.currentThread().getName());
               userService.saveBatch(userList, batchSize);
            });
            list.add(completableFuture);
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[]{}));
        stopWatch.stop();
        System.out.println("插入所需时间为：" + stopWatch.getTotalTimeMillis());
    }
}
