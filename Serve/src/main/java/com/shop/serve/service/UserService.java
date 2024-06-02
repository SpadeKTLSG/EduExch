package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.Result;
import com.shop.pojo.dto.UserGreatDTO;
import com.shop.pojo.dto.UserLoginDTO;
import com.shop.pojo.entity.User;
import jakarta.servlet.http.HttpSession;


public interface UserService extends IService<User> {


    Result login(UserLoginDTO userLoginDTO, HttpSession session);

    Result sendCode(String phone, HttpSession session);

    Result register(UserLoginDTO userLoginDTO, HttpSession session);

    void updateUserGreatDTO(UserGreatDTO userGreatDTO) throws InstantiationException, IllegalAccessException;

    Result sign();

    Result signCount();
}
