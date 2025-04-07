package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;

import java.util.HashMap;
import java.util.Map;

public class TrangThaiUtils {

    private static final Map<String, String> trangThaiMap = new HashMap<>();
    static {
        trangThaiMap.put("CHO_XAC_NHAN", "Chờ xác nhận");
        trangThaiMap.put("XAC_NHAN", "Xác nhận");
        trangThaiMap.put("DANG_CHUAN_BI_HANG", "Đang chuẩn bị hàng");
        trangThaiMap.put("CHO_LAY_HANG", "Chờ lấy hàng");
        trangThaiMap.put("LAY_HANG_THANH_CONG", "Lấy hàng thành công");
        trangThaiMap.put("DANG_VAN_CHUYEN", "Đang vận chuyển");
        trangThaiMap.put("DANG_GIAO", "Đang giao");
        trangThaiMap.put("GIAO_HANG_THANH_CONG", "Giao hàng thành công");
        trangThaiMap.put("GIAO_HANG_THAT_BAI", "Giao hàng thất bại");
        trangThaiMap.put("CHO_CHUYEN_HOAN", "Chờ chuyển hoàn");
        trangThaiMap.put("THANH_CONG", "Thành công");
        trangThaiMap.put("DA_HUY", "Đã hủy");
        trangThaiMap.put("THAT_LAC", "Thất lạc");
        trangThaiMap.put("HOAN_HANG_THANH_CONG", "Hoàn hàng thành công");
    }

    public static String convertTrangThai(String trangThaiKey) {
        return trangThaiMap.getOrDefault(trangThaiKey, trangThaiKey);
    }
}
