package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.entity.UserFollow;
import com.shop.pojo.vo.UserVO;

import java.util.List;

public interface UserFollowService extends IService<UserFollow> {
    // 查阅主表 UserService

    /**
     * 关注/取消关注
     */
    void follow(Long followUserId, Boolean isFollow);

    /**
     * 是否关注
     */
    boolean isFollow(Long followUserId);

    /**
     * 共同关注
     */
    List<UserVO> shareFollow(Long id);
}
