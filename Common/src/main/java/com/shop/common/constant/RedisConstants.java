package com.shop.common.constant;

/**
 * Redis常量
 *
 * @author SK
 * @date 2024/06/05
 */
public class RedisConstants {

    /**
     * 验证码Key前缀 login:code:
     */
    public static final String LOGIN_CODE_KEY = "login:code:";

    /**
     *
     */
    public static final Long LOGIN_CODE_TTL = 172800L; // 2天

    /**
     * 登录接受Key前缀 login:token:
     */
    public static final String LOGIN_USER_KEY = "login:token:";

    /**
     *
     */
    public static final Long LOGIN_USER_TTL = 36000L; // 10小时
    /**
     *
     */
    public static final String USER_SIGN_KEY = "sign:";

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
