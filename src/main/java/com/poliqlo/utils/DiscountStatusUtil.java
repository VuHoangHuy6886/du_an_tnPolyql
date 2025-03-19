package com.poliqlo.utils;

import java.time.LocalDateTime;

public class DiscountStatusUtil {
    public static final String CHUA_DIEN_RA = "CHUA_DIEN_RA";
    public static final String DANG_DIEN_RA = "DANG_DIEN_RA";
    public static final String DA_KET_THUC = "DA_KET_THUC";
    public static final String HUY_BO = "HUY_BO";

    public static String getStatus(LocalDateTime startTime, LocalDateTime endTime, boolean isCancelled) {
        LocalDateTime now = LocalDateTime.now();

        if (isCancelled) {
            return HUY_BO;
        }
        if (now.isBefore(startTime)) {
            return CHUA_DIEN_RA;
        }
        if (now.isAfter(endTime)) {
            return DA_KET_THUC;
        }
        return DANG_DIEN_RA;
    }
}
