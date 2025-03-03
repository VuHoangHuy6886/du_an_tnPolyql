package com.poliqlo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @JoinColumn(name = "THUONG_HIEU_ID")
    private ThuongHieu thuongHieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHAT_LIEU_ID")
    private ChatLieu chatLieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KIEU_DANG_ID")
    private KieuDang kieuDang;

    @Size(max = 50)
    @NotNull
    @Column(name = "MA_SAN_PHAM", nullable = false, length = 50)
    private String maSanPham;

    @Size(max = 255)
    @NotNull
    @Column(name = "TEN", nullable = false)
    private String ten;

    @Size(max = 50)
    @Column(name = "TRANG_THAI", length = 50)
    private String trangThai;

    
    @Column(name = "MO_TA",columnDefinition = "TEXT")
    private String moTa;

    @Size(max = 255)
    @Column(name = "ANH_URL")
    private String anhUrl;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SanPhamChiTiet> sanPhamChiTiets = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "san_pham_danh_muc",
            joinColumns = @JoinColumn(name = "SAN_PHAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "DANH_MUC_ID"))
    private Set<DanhMuc> danhMucs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Anh> anhs = new ArrayList<>();

}