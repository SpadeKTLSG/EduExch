package com.shop.common.context;


//存储当前处理进程的id
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
//        return threadLocal.get(); FIXME
        return 1L;
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
