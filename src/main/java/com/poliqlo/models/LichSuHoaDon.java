package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "lich_su_hoa_don")
public class LichSuHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_HOA_DON", nullable = false)
    private HoaDon idHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TAI_KHOAN")
    private TaiKhoan idTaiKhoan;

    @Column(name = "TIEU_DE", nullable = false)
    private String tieuDe;

    @Lob
    @Column(name = "MO_TA",length = 500)
    private String moTa;

    @Column(name = "THOI_GIAN", nullable = false)
    private Instant thoiGian;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}