package com.poliqlo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "MA", nullable = false, length = 50)
    private String ma;

    @Size(max = 255)
    @NotNull
    @Column(name = "TEN", nullable = false)
    private String ten;

    @NotNull
    @Column(name = "SO_LUONG", nullable = false)
    private Integer soLuong;

    @Column(name = "HOA_DON_TOI_THIEU", precision = 15, scale = 2)
    private BigDecimal hoaDonToiThieu;

    @Size(max = 50)
    @Column(name = "LOAI_HINH_GIAM", length = 50)
    private String loaiHinhGiam;

    @Column(name = "GIA_TRI_GIAM", precision = 15, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "GIAM_TOI_DA", precision = 15, scale = 2)
    private BigDecimal giamToiDa;

    @NotNull
    @Column(name = "NGAY_BAT_DAU", nullable = false)
    private Instant ngayBatDau;

    @NotNull
    @Column(name = "NGAY_KET_THUC", nullable = false)
    private Instant ngayKetThuc;

    @Size(max = 50)
    @Column(name = "TRANG_THAI", length = 50)
    private String trangThai;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}