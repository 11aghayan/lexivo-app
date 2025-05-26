package com.lexivo.util;

public abstract class StringUtil {
    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
