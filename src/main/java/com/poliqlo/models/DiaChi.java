package com.poliqlo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "dia_chi")
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "KHACH_HANG_ID", nullable = false)
    @JsonIgnore
    private KhachHang khachHang;

    @Size(max = 255)
    @NotNull
    @Column(name = "HO_TEN_NGUOI_NHAN", nullable = false)
    private String hoTenNguoiNhan;

    @Size(max = 20)
    @NotNull
    @Column(name = "SO_DIEN_THOAI", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Size(max = 20)
    @Column(name = "WARD_CODE", length = 20)
    private String wardCode;

    @Size(max = 255)
    @NotNull
    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DEFAULT", nullable = false)
    private Boolean isDefault = false;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}