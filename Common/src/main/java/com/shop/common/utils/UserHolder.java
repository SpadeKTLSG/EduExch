package com.shop.common.utils;

import com.shop.pojo.dto.UserLocalDTO;

public class UserHolder {
    private static final ThreadLocal<UserLocalDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserLocalDTO user) {
        tl.set(user);
    }

    public static UserLocalDTO getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }
}
