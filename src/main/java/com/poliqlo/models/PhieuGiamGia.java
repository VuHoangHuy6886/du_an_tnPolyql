package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "MA", nullable = false, length = 50)
    private String ma;

    @Column(name = "TEN", nullable = false)
    private String ten;

    @Column(name = "SO_LUONG", nullable = false)
    private Integer soLuong;

    @Column(name = "HOA_DON_TOI_THIEU", precision = 15, scale = 2)
    private BigDecimal hoaDonToiThieu;

    @Column(name = "LOAI_HINH_GIAM", length = 50)
    private String loaiHinhGiam;

    @Column(name = "GIA_TRI_GIAM", precision = 15, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "GIAM_TOI_DA", precision = 15, scale = 2)
    private BigDecimal giamToiDa;

    @Column(name = "NGAY_BAT_DAU", nullable = false)
    private Instant ngayBatDau;

    @Column(name = "NGAY_KET_THUC", nullable = false)
    private Instant ngayKetThuc;

    @Column(name = "DOI_TUONG_GIAM")
    private String doiTuongGiam;

    @Column(name = "TRANG_THAI", length = 50)
    private String trangThai;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}