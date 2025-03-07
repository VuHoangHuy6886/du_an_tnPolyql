package com.poliqlo.utils;

import java.time.LocalDateTime;
import java.util.Objects;

public class CouponStatusUtil {
    public static final String SAP_DIEN_RA = "Sắp diễn ra";
    public static final String DANG_DIEN_RA = "HOAT_DONG";
    public static final String DA_KET_THUC = "Đã kết thúc";

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
