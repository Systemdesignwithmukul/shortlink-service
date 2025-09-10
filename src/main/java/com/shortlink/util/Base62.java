package com.shortlink.util;

public class Base62 {
    private static final char[] BASE_62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    // Private constructor to prevent instantiation
    private Base62() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.insert(0, BASE_62[(int) (num % 62)]);
            num /= 62;
        }
        return sb.toString();
    }
}
