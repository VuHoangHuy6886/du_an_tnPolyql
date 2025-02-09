package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "hoa_don_chi_tiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_HOA_DON", nullable = false)
    private HoaDon idHoaDon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SAN_PHAM_CHI_TIET", nullable = false)
    private SanPhamChiTiet idSanPhamChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOT_GIAM_GIA")
    private DotGiamGia idDotGiamGia;

    @Column(name = "GIA_GOC", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaGoc;

    @Column(name = "GIA_KHUYEN_MAI", precision = 15, scale = 2)
    private BigDecimal giaKhuyenMai;

    @Column(name = "SO_LUONG", nullable = false)
    private Integer soLuong;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}