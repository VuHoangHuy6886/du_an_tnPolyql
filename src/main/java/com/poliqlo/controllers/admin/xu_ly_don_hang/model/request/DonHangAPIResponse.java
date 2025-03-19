package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;


import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonHangAPIResponse implements Serializable {
    private Integer id;
    private KhachHangDTO khachHang;
    private PhieuGiamGiaDTO phieuGiamGia;
    private String phuongThucThanhToan;
    private BigDecimal phiVanChuyen;
    private BigDecimal tongTien;
    private BigDecimal giamMaGiamGia;
    private String soDienThoai;
    private String diaChi;
    private String tenNguoiNhan;
    private LocalDate ngayNhanHang;
    private String ghiChu;
    private String loaiHoaDon;
    private String trangThai;
    private Boolean isDeleted;
    private List<DonHangChiTietDTO> chiTiet;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KhachHangDTO implements Serializable {
        private Integer id;
        private String ten;
        private LocalDate ngaySinh;
        private String gioiTinh;
        private Integer soLanTuChoiNhanHang;
    }

    // CHÚ Ý: public class => truy cập được ở package khác
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhieuGiamGiaDTO implements Serializable {
        private Integer id;
        private String ma;
        private String ten;
        private BigDecimal giaTriGiam;
        private LocalDateTime thoiGianBatDau;
        private LocalDateTime thoiGianKetThuc;
        private String loaiChietKhau;
        private BigDecimal giamToiDa;
        private String trangThai;
        private Boolean isDeleted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonHangChiTietDTO implements Serializable {
        private Integer id;
        private Integer sanPhamChiTietId;
        private SanPhamDTO sanPham;
        private KichThuocDTO kichThuoc;
        private MauSacDTO mauSac;
        private BigDecimal giaGoc;
        private BigDecimal giaKhuyenMai;
        private Integer soLuong;
        private Boolean isDeleted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SanPhamDTO implements Serializable {
        private Integer id;
        private String maSanPham;
        private String ten;
        private String anhUrl;
        private String trangThai;
        private String moTa;
        private ThuongHieuDTO thuongHieu;
        private ChatLieuDTO chatLieu;
        private KieuDangDTO kieuDang;
        private Boolean isDeleted;
        private List<AnhDTO> anhs;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThuongHieuDTO implements Serializable {
        private Integer id;
        private String ten;
        private String thumbnail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatLieuDTO implements Serializable {
        private Integer id;
        private String ten;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KieuDangDTO implements Serializable {
        private Integer id;
        private String ten;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KichThuocDTO implements Serializable {
        private Integer id;
        private String ten;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MauSacDTO implements Serializable {
        private Integer id;
        private String ten;
        private String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnhDTO implements Serializable {
        private Integer id;
        private Integer mauSacId;
        private Boolean isDefault;
        private String url;
        private Boolean isDeleted;
    }
}
