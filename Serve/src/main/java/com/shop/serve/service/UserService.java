package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.UserGreatDTO;
import com.shop.pojo.dto.UserLoginDTO;
import com.shop.pojo.entity.User;
import com.shop.pojo.vo.UserGreatVO;
import com.shop.pojo.vo.UserVO;
import jakarta.servlet.http.HttpSession;


public interface UserService extends IService<User> {

    String login(UserLoginDTO userLoginDTO, HttpSession session);

    String sendCode(String phone, HttpSession session);

    void register(UserLoginDTO userLoginDTO, HttpSession session);

    void updateUserGreatDTO(UserGreatDTO userGreatDTO) throws InstantiationException, IllegalAccessException;

    void sign();

    int signCount();

    UserVO getByAccount(String account);

    UserVO getByUserId(Long id);

    UserGreatVO info();

    void killMyAccount();

    void logout();

    void updateUserCode(UserLoginDTO userLoginDTO);
}
