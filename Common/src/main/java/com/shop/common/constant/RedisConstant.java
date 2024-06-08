package com.shop.common.constant;

/**
 * Redis常量
 *
 * @author SK
 * @date 2024/06/05
 */
public class RedisConstant {


    //* Admin


    /**
     * 验证码Key前缀
     */
    public static final String LOGIN_CODE_KEY_ADMIN = "admin:login:code:";

    /**
     * 验证码过期时间
     */
    public static final Long LOGIN_CODE_TTL_ADMIN = 1800L; // 30分钟

    /**
     * 登录接受Key前缀
     */
    public static final String LOGIN_USER_KEY_ADMIN = "admin:login:token:";

    /**
     * 登录用户过期时间
     */
    public static final Long LOGIN_USER_TTL_ADMIN = 36000L; // 10小时


    //* Guest

    /**
     * 验证码Key前缀
     */
    public static final String LOGIN_CODE_KEY_GUEST = "guest:login:code:";

    /**
     * 验证码过期时间
     */
    public static final Long LOGIN_CODE_TTL_GUEST = 1800L; // 30分钟

    /**
     * 登录TOKEN Key前缀
     * <p>单机状态下默认清理掉所有的登陆TOKEN</p>
     */
    public static final String LOGIN_USER_KEY_GUEST = "guest:login:token:";

    /**
     * 登录用户过期时间
     */
    public static final Long LOGIN_USER_TTL_GUEST = 36000L; // 10小时


    /**
     * 用户签到Key前缀
     */
    public static final String USER_SIGN_KEY = "sign:";


    /**
     * 用户VO浏览Key前缀
     */
    public static final String USER_VO_KEY = "vo:";

    /**
     * 用户收藏Key前缀
     */
    public static final String PROD_COLLECT_KEY = "guest:prod:collect:";


    //* ZEN - 秒杀逻辑 -

    /**
     * 商品秒杀逻辑库存Key前缀
     */
    public static final String SECKILL_STOCK_KEY = "seckill:stock:";


    /**
     * 商品秒杀逻辑订单Key前缀
     */
    public static final String SECKILL_ORDER_KEY = "seckill:order:";


    /**
     * 缓存空数据时间
     */
    public static final Long CACHE_NULL_TTL = 2L;

    /**
     * 缓存卖家Key前缀
     */
    public static final String LOCK_SHOP_KEY = "lock:shop:";


    //* Ultra - 限流逻辑 -

    /**
     * 发送验证码时间Key前缀
     */
    public static final String SENDCODE_SENDTIME_KEY = "sms:sendtime:";

    /**
     * 一级限流Key前缀
     */
    public static final String ONE_LEVERLIMIT_KEY = "limit:onelevel:";

    /**
     * 二级限流Key前缀
     */
    public static final String TWO_LEVERLIMIT_KEY = "limit:twolevel:";

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";

    public static final Long LOCK_SHOP_TTL = 10L;


}
