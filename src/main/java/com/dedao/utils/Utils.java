package com.dedao.utils;

/**
 * Created by liushuo on 2017/6/1.
 */

public class Utils {
    public static boolean stringIsEmpty(String str) {
        if (str == null) return true;
        if (str.isEmpty()) return true;
        if (str.trim().isEmpty()) return true;

        return false;
    }
}
