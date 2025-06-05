package com.lexivo.util;

import androidx.annotation.NonNull;

public abstract class StringUtil {
    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void setStringBuilderValue(StringBuilder sb, String textToAssign) {
        if (textToAssign == null) {
            textToAssign = "";
        }
        sb.replace(0, sb.length(), textToAssign);
    }

    public static String getValueFromStringBuilder(StringBuilder sb) {
        if (sb.toString().isEmpty()) return null;
        return sb.toString();
    }
}
