package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TAI_KHOAN_ID", nullable = false)
    private TaiKhoan taiKhoan;

    @Column(name = "TEN", nullable = false)
    private String ten;

    @Column(name = "NGAY_SINH")
    private LocalDate ngaySinh;

    @Column(name = "GIOI_TINH", length = 10)
    private String gioiTinh;

    @ColumnDefault("0")
    @Column(name = "SO_LAN_TU_CHOI_NHAN_HANG")
    private Integer soLanTuChoiNhanHang;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}