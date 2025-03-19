package com.poliqlo.utils;

import java.time.LocalDateTime;
import java.util.Objects;

public class CouponStatusUtil {
    public static final String SAP_DIEN_RA = "SAP_DIEN_RA";
    public static final String DANG_DIEN_RA = "DANG_DIEN_RA";
    public static final String DA_KET_THUC = "DA_KET_THUC";

    public static String getStatus(LocalDateTime startTime, LocalDateTime endTime) {
        Objects.requireNonNull(startTime, "startTime không được null");
        Objects.requireNonNull(endTime, "endTime không được null");

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startTime)) {
            return SAP_DIEN_RA;
        }
        if (now.isAfter(endTime)) {
            return DA_KET_THUC;
        }
        return DANG_DIEN_RA;
    }
}
