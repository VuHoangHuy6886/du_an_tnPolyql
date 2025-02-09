package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "dia_chi")
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_KHACH_HANG", nullable = false)
    private KhachHang idKhachHang;

    @Column(name = "HO_TEN_NGUOI_NHAN", nullable = false)
    private String hoTenNguoiNhan;

    @Column(name = "SO_DIEN_THOAI", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Column(name = "WARD_CODE", length = 20)
    private String wardCode;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DEFAULT", nullable = false)
    private Boolean isDefault = false;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}