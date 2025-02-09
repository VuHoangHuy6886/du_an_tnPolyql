package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_KHACH_HANG", nullable = false)
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PHIEU_GIAM_GIA")
    private PhieuGiamGia idPhieuGiamGia;

    @Column(name = "PHUONG_THUC_THANH_TOAN", nullable = false)
    private String phuongThucThanhToan;

    @Column(name = "PHI_VAN_CHUYEN", precision = 15, scale = 2)
    private BigDecimal phiVanChuyen;

    @Column(name = "TONG_TIEN", nullable = false, precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "GIAM_MA_GIAM_GIA", precision = 15, scale = 2)
    private BigDecimal giamMaGiamGia;

    @Column(name = "SO_DIEN_THOAI", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "DIA_CHI", nullable = false)
    private String diaChi;

    @Column(name = "TEN_NGUOI_NHAN", nullable = false)
    private String tenNguoiNhan;

    @Column(name = "NGAY_NHAN_HANG")
    private LocalDate ngayNhanHang;

    @Lob
    @Column(name = "GHI_CHU",length = 500)
    private String ghiChu;

    @Column(name = "LOAI_HOA_DON")
    private String loaiHoaDon;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}