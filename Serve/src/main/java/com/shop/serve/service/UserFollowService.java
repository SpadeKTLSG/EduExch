package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.Result;
import com.shop.pojo.entity.UserFollow;

public interface UserFollowService extends IService<UserFollow> {
    Result follow(Long followUserId, Boolean isFollow);

    Result isFollow(Long followUserId);

    Result shareFollow(Long id);
}
