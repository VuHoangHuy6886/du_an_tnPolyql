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
@Table(name = "dot_giam_gia")
public class DotGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "MA", nullable = false, length = 50)
    private String ma;

    @Column(name = "TEN", nullable = false)
    private String ten;

    @Lob
    @Column(name = "MO_TA", length = 500)
    private String moTa;

    @Column(name = "THOI_GIAN_BAT_DAU", nullable = false)
    private Instant thoiGianBatDau;

    @Column(name = "THOI_GIAN_KET_THUC", nullable = false)
    private Instant thoiGianKetThuc;

    @Column(name = "LOAI_CHIET_KHAU", length = 50)
    private String loaiChietKhau;

    @Column(name = "GIA_TRI_GIAM", precision = 15, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "GIAM_TOI_DA", precision = 15, scale = 2)
    private BigDecimal giamToiDa;

    @Column(name = "TRANG_THAI", length = 50)
    private String trangThai;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}