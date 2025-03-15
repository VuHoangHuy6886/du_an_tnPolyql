package com.poliqlo.controllers.admin.ban_hang.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.poliqlo.models.HoaDon}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HoaDonDto implements Serializable {
    Integer id;
    Integer khachHangId;
    Integer phieuGiamGiaId;
    @NotNull
    @Size(max = 255)
    String phuongThucThanhToan;
    BigDecimal phiVanChuyen;
    @NotNull
    BigDecimal tongTien;
    BigDecimal giamMaGiamGia;
    @NotNull
    @Size(max = 20)
    String soDienThoai;
    @NotNull
    @Size(max = 255)
    String diaChi;
    @NotNull
    @Size(max = 255)
    String tenNguoiNhan;
    LocalDate ngayNhanHang;
    String ghiChu;
    @Size(max = 255)
    String loaiHoaDon;
    @Size(max = 255)
    String trangThai;
    @NotNull
    Boolean isDeleted;
    List<HoaDonChiTietDto> hoaDonChiTiets;

    /**
     * DTO for {@link com.poliqlo.models.HoaDonChiTiet}
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class HoaDonChiTietDto implements Serializable {
        Integer id;
        Integer sanPhamChiTietId;
        Integer dotGiamGiaId;
        @NotNull
        BigDecimal giaGoc;
        BigDecimal giaKhuyenMai;
        @NotNull
        Integer soLuong;


    }
}