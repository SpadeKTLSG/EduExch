package com.shop.serve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.pojo.dto.ProdLocateDTO;
import com.shop.pojo.dto.UserGreatDTO;
import com.shop.pojo.dto.UserLoginDTO;
import com.shop.pojo.entity.Prod;
import com.shop.pojo.entity.User;
import com.shop.pojo.vo.UserGreatVO;
import com.shop.pojo.vo.UserVO;
import jakarta.servlet.http.HttpSession;


public interface UserService extends IService<User> {


    //! Func

    /**
     * 发送手机验证码
     *
     * @return 验证码
     */
    String sendCodeG(String phone, HttpSession session);


    /**
     * 登录
     *
     * @return Token
     */
    String loginG(UserLoginDTO userLoginDTO, HttpSession session);


    /**
     * 注销
     */
    void logoutG();


    /**
     * 签到
     */
    void doSignG();


    /**
     * 签到计数
     */
    int signCountG();


    /**
     * 收藏
     */
    void doCollectG(ProdLocateDTO prodLocateDTO);


    /**
     * 收藏计数
     */
    int collectCountG();


    /**
     * 分页收藏
     */
    Page<Prod> pageCollectG(Integer current);


    //! ADD


    /**
     * 注册
     */
    void registerG(UserLoginDTO userLoginDTO, HttpSession session);


    //! DELETE


    /**
     * 删除用户
     */
    void deleteUserB();


    //! UPDATE

    /**
     * 更新用户信息
     */
    void putUserB(UserGreatDTO userGreatDTO) throws InstantiationException, IllegalAccessException;


    /**
     * 更新用户密码
     */
    void putUserPasswordG(UserLoginDTO userLoginDTO);


    //! QUERY


    /**
     * ID查用户
     */
    UserVO getUser8EzIdA(Long id);


    /**
     * Account查用户
     */
    UserVO getUser8EzA(String account);


    /**
     * 查自己全部信息
     */
    UserGreatVO getUser4MeG();


    /**
     * Account模糊搜索用户
     */
    Page<UserVO> searchUserB(String account, Integer current);
}
