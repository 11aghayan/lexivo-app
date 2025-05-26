package com.lexivo.util;

import com.lexivo.data.LanguageElement;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public abstract class HashUtil {
    public static String generateMd5HashFromString(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            BigInteger bigint = new BigInteger(1, md.digest(s.getBytes()));
            String hexText = bigint.toString(16);
            while (hexText.length() < 32) {
                hexText = "0".concat(hexText);
            }
            return hexText;
        } catch (NoSuchAlgorithmException ignore) {
            return "";
        }
    }

    public static boolean isHashInList(List<? extends LanguageElement> list, String hash) {
        return list.stream().anyMatch(w -> w.getHash().equals(hash));
    }
}
