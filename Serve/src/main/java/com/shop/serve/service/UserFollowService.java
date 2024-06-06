package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.UserLocalDTO;
import com.shop.pojo.entity.UserFollow;

import java.util.List;

public interface UserFollowService extends IService<UserFollow> {

    void follow(Long followUserId, Boolean isFollow);

    boolean isFollow(Long followUserId);

    List<UserLocalDTO> shareFollow(Long id);
}
