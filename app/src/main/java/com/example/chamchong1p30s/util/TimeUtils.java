package com.example.chamchong1p30s.util;

public class TimeUtils {
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
