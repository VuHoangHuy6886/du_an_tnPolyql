package com.poliqlo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "san_pham_ua_thich")
public class SanPhamUaThich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "KHACH_HANG_ID", nullable = false)
    private KhachHang khachHang;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SAN_PHAM_ID", nullable = false)
    private SanPham sanPham;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}