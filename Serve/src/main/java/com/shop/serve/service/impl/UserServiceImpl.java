package com.shop.serve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.constant.PasswordConstant;
import com.shop.common.context.UserHolder;
import com.shop.common.exception.*;
import com.shop.common.utils.RegexUtils;
import com.shop.pojo.dto.*;
import com.shop.pojo.entity.User;
import com.shop.pojo.entity.UserDetail;
import com.shop.pojo.entity.UserFunc;
import com.shop.pojo.vo.UserGreatVO;
import com.shop.pojo.vo.UserVO;
import com.shop.serve.mapper.UserMapper;
import com.shop.serve.service.UserDetailService;
import com.shop.serve.service.UserFuncService;
import com.shop.serve.service.UserService;
import com.shop.serve.tool.NewDTOUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.shop.common.constant.MessageConstants.*;
import static com.shop.common.constant.RedisConstants.*;
import static com.shop.common.utils.NewBeanUtils.dtoMapService;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserFuncService userFuncService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private NewDTOUtils dtoUtils;


    @Transactional
    @Override
    public void register(UserLoginDTO userLoginDTO, HttpSession session) {

        String phone = userLoginDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) throw new InvalidInputException(PHONE_INVALID);

        //从redis获取验证码并校验
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = userLoginDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) throw new InvalidInputException(CODE_INVALID);

        //校验账户是否已存在
        User userExist = query().eq("account", userLoginDTO.getAccount()).one();
        if (userExist != null) throw new AccountAlivedException(ACCOUNT_ALIVED);

        //创建账户 + 账户具体信息 + 账户功能信息, 填充必须字段
        User user = User.builder()
                .account(userLoginDTO.getAccount())
                .password(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes())) //默认密码
                .phone(phone)
                .build();
        save(user);

        UserDetail userDetail = UserDetail.builder()
                .school("维也纳艺术学院")
                .createTime(LocalDateTime.now())
                .introduce("这个人很懒，什么都没留下")
                .build();
        userDetailService.save(userDetail);

        UserFunc userFunc = UserFunc.builder()
                .credit(114L)
                .build();
        userFuncService.save(userFunc);
    }

    @Override
    @Transactional

    public void updateUserGreatDTO(UserGreatDTO userGreatDTO) throws InstantiationException, IllegalAccessException {
        //? 联表选择性更新字段示例

        Optional<User> optionalUser = Optional.ofNullable(this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, userGreatDTO.getAccount())));
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }

        // 用Map存储DTO和Service
        Map<Object, IService> dtoServiceMap = new HashMap<>();
        dtoServiceMap.put(createDTOFromUserGreatDTO(userGreatDTO, UserAllDTO.class), this);
        dtoServiceMap.put(createDTOFromUserGreatDTO(userGreatDTO, UserFuncAllDTO.class), userFuncService);
        dtoServiceMap.put(createDTOFromUserGreatDTO(userGreatDTO, UserDetailAllDTO.class), userDetailService);

        dtoMapService(dtoServiceMap, optionalUser.get().getId(), optionalUser);
    }


    /**
     * 从UserGreatDTO创建DTO
     */
    @SuppressWarnings("deprecation")
    private <T> T createDTOFromUserGreatDTO(UserGreatDTO userGreatDTO, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T dto = clazz.newInstance();
        BeanUtils.copyProperties(userGreatDTO, dto);
        return dto;
    }

    @Override
    public String login(UserLoginDTO userLoginDTO, HttpSession session) {

        //删除掉之前的所有登陆令牌
        Set<String> keys = stringRedisTemplate.keys(LOGIN_USER_KEY + "*");
        if (keys != null) {
            stringRedisTemplate.delete(keys);
        }

        String phone = userLoginDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) throw new InvalidInputException(PHONE_INVALID);

        //从redis获取验证码并校验
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = userLoginDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) throw new InvalidInputException(CODE_INVALID);

        //根据用户名查询用户
        User user = query().eq("account", userLoginDTO.getAccount()).one();
        if (user == null) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);



        // 随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        UserLocalDTO userDTO = BeanUtil.copyProperties(user, UserLocalDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));

        // 存储
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);

        return token;
    }


    @Override
    public void logout() {
        //删除掉之前的所有登陆令牌
        Set<String> keys = stringRedisTemplate.keys(LOGIN_USER_KEY + "*");
        if (keys != null) {
            stringRedisTemplate.delete(keys);
        }
    }


    @Override
    public String sendCode(String phone, HttpSession session) {

        if (RegexUtils.isPhoneInvalid(phone)) throw new InvalidInputException(PHONE_INVALID);

        Set<String> keys = stringRedisTemplate.keys(LOGIN_USER_KEY + phone + "*"); //删除之前的验证码
        if (keys != null) {
            stringRedisTemplate.delete(keys);
        }

        String code = RandomUtil.randomNumbers(6); //生成

        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        return code; //调试环境: 返回验证码; 未来引入邮箱发送验证码
    }


    @Override
    public void sign() {

        Long userId = UserHolder.getUser().getId();

        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));  // 拼接key
        String key = USER_SIGN_KEY + userId + keySuffix;
        int dayOfMonth = now.getDayOfMonth();
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);

    }


    @Override
    public int signCount() {

        Long userId = UserHolder.getUser().getId();

        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));  // 拼接key
        String key = USER_SIGN_KEY + userId + keySuffix;
        int dayOfMonth = now.getDayOfMonth();

        // 获取本月截止今天为止的所有的签到记录，返回的是一个十进制的数字 BITFIELD sign:5:202203 GET u14 0
        List<Long> result = stringRedisTemplate.opsForValue().bitField(
                key,
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0)
        );

        if (result == null || result.isEmpty()) { // 没有任何签到结果
            return 0;
        }

        Long num = result.get(0); // 获取签到结果

        if (num == null || num == 0) {
            return 0;
        }

        int count = 0;

        while ((num & 1) != 0) {
            // 让这个数字与1做与运算，得到数字的最后一个bit位  // 判断这个bit位是否为0
            count++;
            num >>>= 1;// 把数字右移一位，抛弃最后一个bit位，继续下一个bit位
        }
        return count;
    }


    @Override
    public UserVO getByAccount(String account) {
        User user = this.getOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));
        if (user == null) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO getByUserId(Long id) {
        User user = this.getById(id);
        if (user == null) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserGreatVO info() {

//        UserLocalDTO userLocalDTO = UserHolder.getUser(); //从ThreadLocal中获取用户信息id
        //测试时使用id = 1测试账号
        UserLocalDTO userLocalDTO = new UserLocalDTO();
        BeanUtils.copyProperties(this.getById(1L), userLocalDTO);

        if (userLocalDTO == null) throw new NotLoginException(USER_NOT_LOGIN);

        if (this.getById(userLocalDTO.getId()) == null) throw new AccountNotFoundException(ACCOUNT_NOT_FOUND);


        UserGreatVO userGreatVO;
        try {
            userGreatVO = dtoUtils.createAndCombineDTOs(UserGreatVO.class, userLocalDTO.getId(), UserAllDTO.class, UserDetailAllDTO.class, UserFuncAllDTO.class);
        } catch (Exception e) {
            throw new BaseException(UNKNOWN_ERROR);
        }

       /* //查联表基础实现
               UserAllDTO userAllDTO = new UserAllDTO();
        UserDetailAllDTO userDetailDTO = new UserDetailAllDTO();
        UserFuncAllDTO userFuncDTO = new UserFuncAllDTO();

        BeanUtils.copyProperties(userService.getById(userLocalDTO.getId()), userAllDTO);
        BeanUtils.copyProperties(userDetailService.getById(userLocalDTO.getId()), userDetailDTO);
        BeanUtils.copyProperties(userFuncService.getById(userLocalDTO.getId()), userFuncDTO);
        //整合到GreatDTO
        BeanUtils.copyProperties(userAllDTO, userGreatVO);
        BeanUtils.copyProperties(userDetailDTO, userGreatVO);
        BeanUtils.copyProperties(userFuncDTO, userGreatVO);*/
        return userGreatVO;
    }


    @Override
    public void killMyAccount() {
        UserLocalDTO userLocalDTO = UserHolder.getUser();
        this.removeById(userLocalDTO.getId());
        userDetailService.removeById(userLocalDTO.getId());
        userFuncService.removeById(userLocalDTO.getId());
    }


}
