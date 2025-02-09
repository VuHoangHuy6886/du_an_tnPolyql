package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "nhan_vien_log")
public class NhanVienLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NHAN_VIEN_ID", nullable = false)
    private NhanVien nhanVien;

    @Lob
    @Column(name = "NOI_DUNG",length = 500)
    private String noiDung;

    @Column(name = "THOI_GIAN", nullable = false)
    private Instant thoiGian;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}