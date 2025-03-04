package com.poliqlo.controllers.admin.ban_hang.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link HoaDon}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class HoaDonTaiQuayAddRequest implements Serializable {
    KhachHangDto khachHang;
    String maGiamGiaId;
    String tenNguoiNhan;
    String soDienThoai;
    Integer tongSanPham;
    BigDecimal giamVoucher;
    BigDecimal phiGiaoHang;
    BigDecimal tongTienHoaDon;
    Instant thoiGianNhanHang;
    String note;
    String email;
    String hinhThucThanhToan;
    List<String> danhSachImei;

    /**
     * DTO for {@link KhachHang}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KhachHangDto implements Serializable {
        Integer id;
        String ten;
        String soDienThoai;
        String email;
    }

}