package com.poliqlo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hoa_don_chi_tiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HOA_DON_ID", nullable = false)
    private HoaDon hoaDon;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SAN_PHAM_CHI_TIET_ID", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOT_GIAM_GIA_ID")
    private DotGiamGia dotGiamGia;

    @NotNull
    @Column(name = "GIA_GOC", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaGoc;

    @Column(name = "GIA_KHUYEN_MAI", precision = 15, scale = 2)
    private BigDecimal giaKhuyenMai;

    @NotNull
    @Column(name = "SO_LUONG", nullable = false)
    private Integer soLuong;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}