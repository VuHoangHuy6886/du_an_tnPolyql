package com.poliqlo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lich_su_hoa_don")
public class LichSuHoaDon {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HOA_DON_ID", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAI_KHOAN_ID")
    private TaiKhoan taiKhoan;

    @Size(max = 255)
    @NotNull
    @Column(name = "TIEU_DE", nullable = false)
    private String tieuDe;

    
    @Column(name = "MO_TA",columnDefinition = "TEXT")
    private String moTa;

    @NotNull
    @Column(name = "THOI_GIAN", nullable = false)
    private LocalDateTime thoiGian;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}