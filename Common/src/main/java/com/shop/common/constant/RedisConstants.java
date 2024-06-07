package com.shop.common.constant;

/**
 * Redis常量
 *
 * @author SK
 * @date 2024/06/05
 */
public class RedisConstants {


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
     * 登录接受Key前缀
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


    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";

    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;

    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String FEED_KEY = "feed:";
    public static final String SHOP_GEO_KEY = "shop:geo:";

}
