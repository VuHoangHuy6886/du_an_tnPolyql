package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_THUONG_HIEU")
    private ThuongHieu idThuongHieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CHAT_LIEU")
    private MauSac idMauSac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_KIEU_DANG")
    private KieuDang idKieuDang;

    @Column(name = "MA_SAN_PHAM", nullable = false, length = 50)
    private String maSanPham;

    @Column(name = "TEN", nullable = false)
    private String ten;

    @Column(name = "TRANG_THAI", length = 50)
    private String trangThai;

    @Lob
    @Column(name = "MO_TA",length = 500)
    private String moTa;

    @Column(name = "ANH_URL")
    private String anhUrl;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}