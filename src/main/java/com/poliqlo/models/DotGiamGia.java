package com.poliqlo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "dot_giam_gia")
public class DotGiamGia {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "MA", nullable = false, length = 50)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Column(name = "TEN", nullable = false)
    private String ten;


    @Column(name = "MO_TA", columnDefinition = "TEXT")
    private String moTa;

    @NotNull
    @Column(name = "THOI_GIAN_BAT_DAU", nullable = false)
    private LocalDateTime thoiGianBatDau;

    @NotNull
    @Column(name = "THOI_GIAN_KET_THUC", nullable = false)
    private LocalDateTime thoiGianKetThuc;

    @Size(max = 50)
    @Column(name = "LOAI_CHIET_KHAU", length = 50)
    private String loaiChietKhau;

    @Column(name = "GIA_TRI_GIAM", precision = 15, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "GIAM_TOI_DA", precision = 15, scale = 2)
    private BigDecimal giamToiDa;

    @Size(max = 50)
    @Column(name = "TRANG_THAI", length = 50)
    private String trangThai;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;
}