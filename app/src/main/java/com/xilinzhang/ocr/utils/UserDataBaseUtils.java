package com.xilinzhang.ocr.utils;

import java.util.Map;

public class UserDataBaseUtils {
    public static final String POST_ADDR = "user";
    public static final String POST_SIGN_IN = "signin";
    public static final String POST_SIGN_UP = "signup";

    public static final String DATABASE_KEY_USER = "user";
    public static final String DATABASE_KEY_PWD = "pwd";

    public static boolean signUp(Map<String, Object> info) {
        return NetworkUtils.sendPost(NetworkUtils.hostAddr + POST_SIGN_UP, info).toLowerCase().contains("success");
    }

    public static boolean signIn(Map<String, Object> info) {
        String result = NetworkUtils.sendPost(NetworkUtils.hostAddr + POST_SIGN_IN, info);
        return result.toLowerCase().contains("success");
    }
}
