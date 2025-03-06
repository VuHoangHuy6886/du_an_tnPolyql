package com.poliqlo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JoinFormula;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SAN_PHAM_ID", nullable = false)
    @JsonIgnore
    private SanPham sanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KICH_THUOC_ID")
    private KichThuoc kichThuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAU_SAC_ID")
    private MauSac mauSac;

    @NotNull
    @Column(name = "GIA_BAN", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaBan;

    @NotNull
    @Column(name = "SO_LUONG", nullable = false)
    private Integer soLuong;

    @Size(max = 100)
    @Column(name = "BARCODE", length = 100)
    private String barcode;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinFormula("(SELECT d.id FROM san_pham_chi_tiet_dot_giam_gia spdgg " +
            "JOIN dot_giam_gia d ON spdgg.dot_giam_gia_id = d.id " +
            "WHERE spdgg.san_pham_chi_tiet_id = id " +
            "AND d.is_deleted = 0 " +
            "AND d.trang_thai = 'HOAT_DONG' " +
            "AND d.thoi_gian_bat_dau <= NOW() " +
            "AND d.thoi_gian_ket_thuc >= NOW() " +
            "ORDER BY spdgg.id " +
            "LIMIT 1)")
    private DotGiamGia dotGiamGia;

//    @OneToMany(mappedBy = "sanPhamChiTiet")
//    private Set<SanPhamChiTietDotGiamGia> sanPhamChiTietDotGiamGias = new LinkedHashSet<>();

}