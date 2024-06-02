package com.shop.serve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.pojo.Result;
import com.shop.pojo.dto.UserLocalDTO;
import com.shop.pojo.entity.UserFollow;
import com.shop.serve.mapper.UserFollowMapper;
import com.shop.serve.service.UserFollowService;
import com.shop.serve.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserService userService;


    @Override
    public Result follow(Long followUserId, Boolean isFollow) {

//        Long userId = UserHolder.getUser().getId(); //获取当前登录用户
        //调试选项:
        Long userId = 1L;
        String key = "follows:" + userId;

        if (isFollow && save(UserFollow.builder() //关注 -> 新增
                .followerId(userId)
                .followedId(followUserId)
                .build())) {

            stringRedisTemplate.opsForSet().add(key, followUserId.toString()); //Redis集合中添加关注用户的id
            return Result.success();
        }

        if (remove(new QueryWrapper<UserFollow>() // 关注 -> 删除
                .eq("follower_id", userId)
                .eq("followed_id", followUserId))) {

            stringRedisTemplate.opsForSet().remove(key, followUserId.toString()); //Redis集合中移除关注用户的id
        }

        return Result.success();
    }

    @Override
    public Result isFollow(Long followUserId) {
//        Long userId = UserHolder.getUser().getId();
        //调试选项:
        Long userId = 1L;

        Long count = query()  // 查询是否关注
                .eq("follower_id", userId)
                .eq("followed_id", followUserId)
                .count();

        return Result.success(count > 0);
    }


    @Override
    public Result shareFollow(Long id) {
//        Long userId = UserHolder.getUser().getId();
        //调试选项:
        Long userId = 1L;
        String key1 = "follows:" + userId;
        String key2 = "follows:" + id;

        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key1, key2); //求交集

        if (intersect == null || intersect.isEmpty()) {
            return Result.success(Collections.emptyList());
        }


        List<Long> ids = intersect.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());  // 解析id集合


        List<UserLocalDTO> users = userService.listByIds(ids)
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserLocalDTO.class))
                .collect(Collectors.toList());   // 查询用户

        return Result.success(users);
    }
}
